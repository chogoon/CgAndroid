package com.chogoon.cglib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Formatter;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class CgUtils {

    public static boolean isNotNull(Object obj) {
        return obj != null;
    }

    public static String pointFormat(int point) {
        return new DecimalFormat("#,###").format(point);
    }

    public static String getStarsStr(float value) {
        return String.format(Locale.getDefault(), "%.1f", value);
    }

    public static String strFormat(String format, Object... args) {
        return new Formatter(Locale.getDefault()).format(format, args).toString();
    }

    public static boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    public static RequestBody createBody(String format, Object... args) {
        return RequestBody.create(MediaType.parse("application/json"), strFormat(format, args));
    }

    public static boolean isNumeric(String value) {
        if (TextUtils.isEmpty(value)) return false;
        return value.matches("-?\\d+(\\.\\d+)?");
    }

    public static Drawable getDrawable(@NonNull Context context, @DrawableRes int id){
        return ActivityCompat.getDrawable(context, id);
    }

    public static int getColor(@NonNull Context context, @ColorRes int id){
        return ActivityCompat.getColor(context, id);
    }

    public static String getTag(Class clazz) {
        return clazz.getSimpleName();
    }

    public static String getSHA256(String str) {
        String encrypt = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes(), 0, str.length());
            encrypt = new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encrypt;
    }
}
