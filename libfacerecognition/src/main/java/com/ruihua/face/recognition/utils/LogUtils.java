package com.ruihua.face.recognition.utils;

import android.text.TextUtils;
import android.util.Log;


public class LogUtils {

    private static final String TAG = "FaceRecognize";
    public static String customTagPrefix = "";

    private LogUtils() {
    }

    public static boolean allowD = true;
    public static boolean allowE = true;
    public static boolean allowI = true;
    public static boolean allowV = true;
    public static boolean allowW = true;
    public static boolean allowWtf = true;

    private static String generateTag(StackTraceElement caller) {
        String TAG = "%s.%s(L:%d)";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        TAG = String.format(TAG, callerClazzName, caller.getMethodName(), caller.getLineNumber());
        TAG = TextUtils.isEmpty(customTagPrefix) ? TAG : customTagPrefix + ":" + TAG;
        return TAG;
    }

    public static CustomLogger customLogger;

    public interface CustomLogger {
        void d(String TAG, String content);

        void d(String TAG, String content, Throwable tr);

        void e(String TAG, String content);

        void e(String TAG, String content, Throwable tr);

        void i(String TAG, String content);

        void i(String TAG, String content, Throwable tr);

        void v(String TAG, String content);

        void v(String TAG, String content, Throwable tr);

        void w(String TAG, String content);

        void w(String TAG, String content, Throwable tr);

        void w(String TAG, Throwable tr);

        void wtf(String TAG, String content);

        void wtf(String TAG, String content, Throwable tr);

        void wtf(String TAG, Throwable tr);
    }

    public static void d(String content) {
        if (!allowD) return;

        if (customLogger != null) {
            customLogger.d(TAG, content);
        } else {
            Log.d(TAG, content);
        }
    }

    public static void d(String content, Throwable tr) {
        if (!allowD) return;

        if (customLogger != null) {
            customLogger.d(TAG, content, tr);
        } else {
            Log.d(TAG, content, tr);
        }
    }

    public static void e(String content) {
        if (!allowE) return;

        if (customLogger != null) {
            customLogger.e(TAG, content);
        } else {
            Log.e(TAG, content);
        }
    }

    public static void e(String content, Throwable tr) {
        if (!allowE) return;

        if (customLogger != null) {
            customLogger.e(TAG, content, tr);
        } else {
            Log.e(TAG, content, tr);
        }
    }

    public static void i(String content) {
        if (!allowI) return;

        if (customLogger != null) {
            customLogger.i(TAG, content);
        } else {
            Log.i(TAG, content);
        }
    }

    public static void i(String content, Throwable tr) {
        if (!allowI) return;

        if (customLogger != null) {
            customLogger.i(TAG, content, tr);
        } else {
            Log.i(TAG, content, tr);
        }
    }

    public static void v(String content) {
        if (!allowV) return;

        if (customLogger != null) {
            customLogger.v(TAG, content);
        } else {
            Log.v(TAG, content);
        }
    }

    public static void v(String content, Throwable tr) {
        if (!allowV) return;

        if (customLogger != null) {
            customLogger.v(TAG, content, tr);
        } else {
            Log.v(TAG, content, tr);
        }
    }

    public static void w(String content) {
        if (!allowW) return;

        if (customLogger != null) {
            customLogger.w(TAG, content);
        } else {
            Log.w(TAG, content);
        }
    }

    public static void w(String content, Throwable tr) {
        if (!allowW) return;

        if (customLogger != null) {
            customLogger.w(TAG, content, tr);
        } else {
            Log.w(TAG, content, tr);
        }
    }

    public static void w(Throwable tr) {
        if (!allowW) return;

        if (customLogger != null) {
            customLogger.w(TAG, tr);
        } else {
            Log.w(TAG, tr);
        }
    }


    public static void wtf(String content) {
        if (!allowWtf) return;

        if (customLogger != null) {
            customLogger.wtf(TAG, content);
        } else {
            Log.wtf(TAG, content);
        }
    }

    public static void wtf(String content, Throwable tr) {
        if (!allowWtf) return;

        if (customLogger != null) {
            customLogger.wtf(TAG, content, tr);
        } else {
            Log.wtf(TAG, content, tr);
        }
    }

    public static void wtf(Throwable tr) {
        if (!allowWtf) return;

        if (customLogger != null) {
            customLogger.wtf(TAG, tr);
        } else {
            Log.wtf(TAG, tr);
        }
    }

}
