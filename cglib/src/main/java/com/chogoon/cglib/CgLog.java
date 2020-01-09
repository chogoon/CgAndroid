package com.chogoon.cglib;


import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class CgLog {

    public static boolean DEBUG = BuildConfig.DEBUG;

    private static final int LENGTH = 1024;

    public static void v(String tag, Object message) {
        if (DEBUG) Log.v(tag, String.valueOf(message));
    }

    public static void d(String tag, Object message) {
        if (DEBUG) Log.d(tag, String.valueOf(message));
    }

    public static void i(String tag, Object message) {
        if (DEBUG) Log.i(tag, String.valueOf(message));
    }

    public static void w(String tag, Object message) {
        if (DEBUG) Log.w(tag, String.valueOf(message));
    }

    public static void e(String tag, Object message) {
        if (DEBUG) {
            String temp = String.valueOf(message);
            while (temp.length() > 0) {
                if (temp.length() > LENGTH) {
                    Log.e(tag, temp.substring(0, LENGTH));
                    temp = temp.substring(LENGTH);
                } else {
                    Log.e(tag, temp);
                    break;
                }
            }
        }
    }

    public static void e(Class<?> clas, Object message) {
        if (DEBUG) Log.e(clas.getSimpleName(), String.valueOf(message));
    }

    public static void w(Class<?> clas, Object message) {
        if (DEBUG) Log.w(clas.getSimpleName(), String.valueOf(message));
    }

    public static void i(Class<?> clas, Object message) {
        if (DEBUG) Log.i(clas.getSimpleName(), String.valueOf(message));
    }

    public static void d(Class<?> clas, Object message) {
        if (DEBUG) Log.d(clas.getSimpleName(), String.valueOf(message));
    }

    public static void v(Class<?> clas, Object message) {
        if (DEBUG) Log.v(clas.getSimpleName(), String.valueOf(message));
    }

    public static void e(final Object message) {
        StackTraceElement[] arrSte = getTrace();
        StackTraceElement ste = getCurInfo(arrSte);
        e(getTag(ste), message + callInfo(arrSte, 1));
    }

    public static void e(Object... message) {
        StackTraceElement[] arrSte = getTrace();
        StackTraceElement ste = getCurInfo(arrSte);
        e(getTag(ste), Arrays.toString(message) + callInfo(arrSte, 1));
    }

    public static void w(final Object message) {
        StackTraceElement[] arrSte = getTrace();
        StackTraceElement ste = getCurInfo(arrSte);
        w(getTag(ste), message + callInfo(arrSte, 1));
    }

    public static void i(final Object message) {
        StackTraceElement[] arrSte = getTrace();
        StackTraceElement ste = getCurInfo(arrSte);
        i(getTag(ste), message + callInfo(arrSte, 1));
    }

    public static void L(Object source) {
        StackTraceElement[] arrSte = getTrace();
        StackTraceElement ste = getCurInfo(arrSte);
        L(getTag(ste), source);
    }

    public static void L(final String tag, Object source) {
        if (!DEBUG) return;
        Object o = getJsonObjFromStr(source);
        if (o != null) {
            if (o instanceof JSONObject) {
                String json = ((JSONObject) o).toString();
                int length = json.length();

                for (int i = 0; i < length; i += 1024) {
                    if (i + 1024 < length)
                        Log.d(tag, json.substring(i, i + 1024));
                    else
                        Log.d(tag, json.substring(i, length));
                }
            } else if (o instanceof JSONArray) {
                String json = ((JSONArray) o).toString();
                int length = json.length();

                for (int i = 0; i < length; i += 1024) {
                    if (i + 1024 < length)
                        Log.d(tag, json.substring(i, i + 1024));
                    else
                        Log.d(tag, json.substring(i, length));
                }
            } else {
                Log.d(tag, "" + source);
            }
        } else {
            Log.d(tag, "" + source);
        }
    }

    private static Object getJsonObjFromStr(Object test) {
        Object o = null;
        try {
            o = new JSONObject(test.toString());
        } catch (JSONException ex) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    o = new JSONArray(test);
                }
            } catch (JSONException ex1) {
                return null;
            }
        }
        return o;
    }

    public static void d(final Object message) {
        StackTraceElement[] arrSte = getTrace();
        StackTraceElement ste = getCurInfo(arrSte);
        d(getTag(ste), message + callInfo(arrSte, 1));
    }

    public static void v(final Object message) {
        StackTraceElement[] arrSte = getTrace();
        StackTraceElement ste = getCurInfo(arrSte);
        v(getTag(ste), message + callInfo(arrSte, 1));
    }


    private static String getExtraInfo(StackTraceElement ste) {
        return ste.getMethodName() + "[" + ste.getLineNumber() + "] ";
    }

    private static StackTraceElement getCurInfo(StackTraceElement[] arrSte) {
        return arrSte[2];
    }

    private static StackTraceElement[] getTrace() {
        return new Throwable().getStackTrace();
    }

    private static String getTag(StackTraceElement ste) {
        return ste.getClassName()
                .replace("com.sirjuseyo", "")
                .replace("com.sirjuseyo.", "")
                .replace("activity.", "")
                .replace("activity", "")
                .replace("fragment.", "")
                .replace("fragment", "");
    }


    private static String callInfo(StackTraceElement[] arrSte, int depth) {
        StringBuilder builder = new StringBuilder();

        int cnt = 0;
        int tdepth = depth <= 0 ? 1 : depth;

        if (depth > 1) {
            builder.append("\nmethod: ");
        } else {
            builder.append(" method: ");
        }

        for (StackTraceElement ste : arrSte) {
            if (cnt < 2) {
                cnt++;
                continue;
            }
            if (tdepth <= 0) break;
            builder.append(getExtraInfo(ste));
            builder.append("\n");
            cnt++;
            tdepth--;
        }
        return builder.toString();
    }
}
