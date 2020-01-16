package com.chogoon.cglib.crytpto

import android.content.Context
import android.util.Base64
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.GeneralSecurityException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

class KCrypto constructor(context: Context) {

    val IV_SIZE = 16
    val KEY_SIZE = 32

    val SECURE_KEYS = arrayOf("secure_key", "temp_key", "temp")
    val context = context

    private fun secureTemp(): ByteArray {
        val temp = ByteArray(IV_SIZE)
        SECURE_KEYS[2].readFromFileOrCreateRandom(temp)
        return temp
    }
    private fun retrieveKey(): ByteArray {
        val key = ByteArray(KEY_SIZE)
        SECURE_KEYS[1].readFromFileOrCreateRandom(key)
        return key
    }

    fun removeFiles(){
        SECURE_KEYS.forEach { s ->
            File(context.filesDir, s).delete()
        }
    }

    private fun String.readBytesFromFile(bytes: ByteArray) {
        try {
            val fis= context.openFileInput(this)
            var numBytes = 0
            while (numBytes < bytes.size) {
                val n = fis.read(bytes, numBytes, bytes.size - numBytes)
                if (n <= 0) {
                    throw RuntimeException("Couldn't read from $this")
                }
                numBytes += n
            }
            fis.close()
        } catch (e: IOException) {
            throw RuntimeException("Couldn't read from $this", e)
        }
    }

    private fun String.readFromFileOrCreateRandom(bytes: ByteArray){
        if(this.fileExists()){
            this.readBytesFromFile(bytes)
            return
        }
        val sr = SecureRandom()
        sr.nextBytes(bytes)
        this.writeToFile(bytes)
    }

    private fun String.writeToFile(bytes: ByteArray) {
        try {
            val fos = context.openFileOutput(this, Context.MODE_PRIVATE)
            fos.write(bytes)
            fos.close()
        } catch (e: IOException) {
            throw java.lang.RuntimeException("Couldn't write to $this", e)
        }
    }

    private fun encryptOrDecrypt(data: ByteArray, key: SecretKey, temp: ByteArray, isEncrypt: Boolean): ByteArray {
        return try {
            val cipher = Cipher.getInstance("AES/CBC/PKCS7PADDING")
            cipher.init(if (isEncrypt) Cipher.ENCRYPT_MODE else Cipher.DECRYPT_MODE, key, IvParameterSpec(temp))
            cipher.doFinal(data)
        } catch (e: GeneralSecurityException) {
            throw RuntimeException("This is unconceivable!", e)
        }
    }

//    private fun encryptData(data: ByteArray, temp: ByteArray, key: SecretKey): ByteArray? {
//        return encryptOrDecrypt(data, key, temp, true)
//    }

//    private fun decryptData(data: ByteArray, temp: ByteArray, key: SecretKey): ByteArray? {
//        return encryptOrDecrypt(data, key, temp, false)
//    }

    open fun storeDataDecryptedWithSecureKey(encryptedData: ByteArray, secureKey: SecretKey): ByteArray {
        return encryptOrDecrypt(encryptedData, secureKey, secureTemp(), false)
    }

    open fun storeDataEncryptedWithSecureKey(encryptedData: ByteArray, secureKey: SecretKey): ByteArray {
        SECURE_KEYS[0].writeToFile(ByteArray(1))
//        return encryptData(encryptedData, secureTemp(), secureKey)
        return encryptOrDecrypt(encryptedData, secureKey, secureTemp(), true)
    }

    private fun String.fileExists(): Boolean {
        val file = File(context.filesDir, this)
        return file.exists()
    }

    fun deriveKeySecurely(password: String): SecretKey? {
        try {
            val keySpec: KeySpec = PBEKeySpec(password.toCharArray(), retrieveKey(), 100, KEY_SIZE * 8)
            val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            val keyBytes = keyFactory.generateSecret(keySpec).encoded
            return SecretKeySpec(keyBytes, "AES")
        } catch (e: Exception) {
        }
        return null
    }

    companion object {


        @JvmStatic
        fun encryptBase(text: String): String {
            val plainText = text.toByteArray()
            val cipherText = ByteArray(text.length + 16)
            var outputLength: Int

            val seed = SEEDCBC()
            seed.init(SEEDCBC.ENC, SEEDCBC.KEY, SEEDCBC.IV)
            outputLength = seed.process(plainText, 0, plainText.size, cipherText, 0)
            outputLength += seed.close(cipherText, outputLength)

            val sliceCipher = ByteArray(outputLength)
            System.arraycopy(cipherText, 0, sliceCipher, 0, outputLength)
            return Base64.encodeToString(sliceCipher, 0)

        }

        @JvmStatic
        fun decryptBase(text: String?): String {
            if (text.isNullOrEmpty()) return ""

            val plainText = ByteArray(144)
            val cipherText = Base64.decode(text, 0)
            val outputLength: Int
            val seed = SEEDCBC()
            seed.init(SEEDCBC.DEC, SEEDCBC.KEY, SEEDCBC.IV)
            outputLength = seed.process(cipherText, 0, cipherText.size, plainText, 0)
            seed.close(plainText, outputLength)
            return String(plainText)

        }
    }

    fun getSHA256(str: String): String {
        var encrypt = ""
        try {
            val md = MessageDigest.getInstance("SHA-256")
            md.update(str.toByteArray(), 0, str.length)
            encrypt = BigInteger(1, md.digest()).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return encrypt
    }
}