package com.chogoon.cglib;

import android.content.Context;

import javax.crypto.SecretKey;


public class CgUserData {
    private static final String TAG = "UserData";

    public static synchronized <T extends BaseUserModel> T load(Context context, Class<T> clazz) {
        CgCrypto mCrypto = new CgCrypto(context);
        try {
            SecretKey secureKey = mCrypto.deriveKeySecurely(PreferencesManager.getBaseKey());
            if (secureKey != null) {
                byte[] json = Installation.getData(context, true, null);
                if (json != null && json.length > 1) {
                    byte[] decrypted = mCrypto.storeDataDecryptedWithSecureKey(json, secureKey);
                    return CgGson.getInstance().fromJson(new String(decrypted), clazz);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized BaseUserModel save(Context context, BaseUserModel data) {
        CgCrypto mCrypto = new CgCrypto(context);
        mCrypto.removeFiles();
        SecretKey secureKey = mCrypto.deriveKeySecurely(PreferencesManager.getBaseKey());
        byte[] encrypted = mCrypto.storeDataEncryptedWithSecureKey(CgGson.getInstance().toJson(data).getBytes(), secureKey);
        Installation.getData(context, false, encrypted);
        return data;
    }

    public static synchronized BaseUserModel reset(Context context) {
        BaseUserModel obj = new BaseUserModel();
//		BaseUserModel temp = load(context);
//		mBean.config = temp.config;
//		mBean.addr = temp.addr;
        return save(context, obj);
    }
}