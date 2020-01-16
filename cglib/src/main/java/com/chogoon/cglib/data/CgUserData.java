package com.chogoon.cglib.data;

import android.content.Context;

import com.chogoon.cglib.BaseUserModel;
import com.chogoon.cglib.CgGson;
import com.chogoon.cglib.crytpto.Installation;
import com.chogoon.cglib.PreferencesManager;
import com.chogoon.cglib.crytpto.KCrypto;

import javax.crypto.SecretKey;


public class CgUserData {
    private static final String TAG = "CgUserData";

    public static synchronized <T extends BaseUserModel> T load(Context context, Class<T> clazz) {
        KCrypto crypto = new KCrypto(context);
        try {
            SecretKey secureKey = crypto.deriveKeySecurely(PreferencesManager.getBaseKey());
            if (secureKey != null) {
                byte[] json = Installation.getData(context, true, null);
                if (json != null && json.length > 1) {
                    byte[] decrypted = crypto.storeDataDecryptedWithSecureKey(json, secureKey);
                    return CgGson.getInstance().fromJson(new String(decrypted), clazz);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized BaseUserModel save(Context context, BaseUserModel data) {
        KCrypto crypto = new KCrypto(context);
        crypto.removeFiles();
        SecretKey secureKey = crypto.deriveKeySecurely(PreferencesManager.getBaseKey());
        byte[] encrypted = crypto.storeDataEncryptedWithSecureKey(CgGson.getInstance().toJson(data).getBytes(), secureKey);
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