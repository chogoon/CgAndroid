package com.chogoon.cglib.crytpto;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by chogoon on 2016-10-20.
 */

@Deprecated
public class CgCrypto {

    private static final int IV_SIZE = 16;
    private static final int KEY_SIZE = 32;

    private final String[] SECURE_KEYS = {"secure_key", "temp_key", "temp"};
    private Context mContext;

    public CgCrypto(Context mContext) {
        this.mContext = mContext;
    }

    private byte[] secureTemp() {
        byte[] temp = new byte[IV_SIZE];
        readFromFileOrCreateRandom(SECURE_KEYS[2], temp);
        return temp;
    }

    private byte[] retrieveKey() {
        byte[] key = new byte[KEY_SIZE];
        readFromFileOrCreateRandom(SECURE_KEYS[1], key);
        return key;
    }

    public void removeFiles() {
        for (String secure_key : SECURE_KEYS) {
            File file = new File(mContext.getFilesDir(), secure_key);
            file.delete();
        }
    }

    private void readBytesFromFile(String fileName, byte[] bytes) {
        try {
            FileInputStream fis = mContext.openFileInput(fileName);
            int numBytes = 0;
            while (numBytes < bytes.length) {
                int n = fis.read(bytes, numBytes, bytes.length - numBytes);
                if (n <= 0) {
                    throw new RuntimeException("Couldn't read from " + fileName);
                }
                numBytes += n;
            }
            if(fis != null) fis.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't read from " + fileName, e);
        }
    }

    private void writeToFile(String fileName, byte[] bytes) {
        try {
            FileOutputStream fos = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(bytes);
            if(fos != null) fos.close();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't write to " + fileName, e);
        }
    }

    private void readFromFileOrCreateRandom(String fileName, byte[] bytes) {
        if (fileExists(fileName)) {
            readBytesFromFile(fileName, bytes);
            return;
        }
        SecureRandom sr = new SecureRandom();
        sr.nextBytes(bytes);
        writeToFile(fileName, bytes);
    }

    private boolean fileExists(String fileName) {
        File file = new File(mContext.getFilesDir(), fileName);
        return file.exists();
    }

    private byte[] encryptOrDecrypt(byte[] data, SecretKey key, byte[] temp, boolean isEncrypt) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING");
            cipher.init(isEncrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key, new IvParameterSpec(temp));
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("This is unconceivable!", e);
        }
    }

    private byte[] encryptData(byte[] data, byte[] temp, SecretKey key) {
        return encryptOrDecrypt(data, key, temp, true);
    }

    private byte[] decryptData(byte[] data, byte[] temp, SecretKey key) {
        return encryptOrDecrypt(data, key, temp, false);
    }

    public byte[] storeDataDecryptedWithSecureKey(byte[] encryptedData, SecretKey secureKey) {
        return decryptData(encryptedData, secureTemp(), secureKey);
    }

    public byte[] storeDataEncryptedWithSecureKey(byte[] encryptedData, SecretKey secureKey) {
        writeToFile(SECURE_KEYS[0], new byte[1]);
        return encryptData(encryptedData, secureTemp(), secureKey);
    }

    public SecretKey deriveKeySecurely(String password) {
        try {
            KeySpec keySpec = new PBEKeySpec(password.toCharArray(), retrieveKey(), 100, KEY_SIZE * 8);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {

        }
        return null;
    }

}
