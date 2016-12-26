package com.dc.hwallpaper.utils;

import android.util.Log;

/**
 * Created by Li DaChang on 16/12/25.
 * ..-..---.-.--..---.-...-..-....-.
 */

public class Logger {
    private static final String DEFAULT_FLAG = "ssss---";
    private static final String DEFAULT_FLAG_2 = "xxxx---";
    private static final String DC_TAG_1 = "---";
    private static final String DC_TAG_2 = "-+-+- ";
    private static final boolean IS_DEBUG = true;

    /**
     * 原始打印方法
     *
     * @param flag flag
     * @param msg  信息
     */
    private static void takeLog(String flag, String className, String msg) {
        if (IS_DEBUG) {
            //为了看起来养眼,对类名前缀的长度进行统一:太长的进行压缩,短的进行补位
            if (className.length() > 17) {
                className = className.substring(0, 15) + "...";
            }
            className = String.format("%-20s", className);//补位,不足20位的用空格填充
            className = className + ": ";
            Log.i(flag, className + msg);
        }
    }

    public static void w(Object msg) {
        if (IS_DEBUG) {
            String className = getClassName();
            //为了看起来养眼,对类名前缀的长度进行统一:太长的进行压缩,短的进行补位
            if (className.length() > 18) {
                className = className.substring(0, 15) + "...";
            }
            className = String.format("%-20s", className);//补位,不足20位的用空格填充
            className = className + ": ";
            Log.w(DC_TAG_1, className + String.valueOf(msg));
        }
    }

    public static void e(Object msg) {
        if (IS_DEBUG) {
            Log.e(DC_TAG_1, getClassName() + "-------" + String.valueOf(msg));
        }
    }

    /**
     * 打印分割线,主要用于程序启动或销毁时在log中做标记
     *
     * @param msg1 信息
     * @param time 当前时间
     */
    public static void line(String msg1, String time) {
        takeLog(DC_TAG_1, getClassName() + "\n",
                "==========================================\n" +
                        "\t我是" + msg1 + "的分割线-" + time +
                        "\n==========================================");
    }

    public static void i(String flag, String msg) {
        takeLog(flag, getClassName(), msg);
    }

    public static void i(String msg) {
        takeLog(DC_TAG_1, getClassName(), msg);
    }

    public static void i(Object msg) {
        takeLog(DC_TAG_1, getClassName(), String.valueOf(msg));
    }

    public static void i(int i) {
        takeLog(DC_TAG_1, getClassName(), "-----------" + i);
    }

    public static void i(String flag, String msgKey, Object msgValue) {
        takeLog(flag, getClassName(), flag + " " + msgKey + " : " + String.valueOf(msgValue) + " --;");
    }

    public static void i(String flag, Object msg) {
        takeLog(flag, getClassName(), String.valueOf(msg));
    }

    public static void i(int flag, Object msg) {
        if (flag == 1) {
            takeLog(DC_TAG_1, getClassName(), String.valueOf(msg));
        }
    }


    public static void i(int tag, String msgKey, Object msgValue) {
        if (tag == 1) {
            takeLog(DC_TAG_1, getClassName(), DC_TAG_1 + " " + msgKey + " : " + String.valueOf(msgValue) + " --");
        }
    }

    public static void i(int tag, String key1, Object value1, String key2, Object value2) {
        if (tag == 1) {
            takeLog(DC_TAG_1, getClassName(), key1 + ":" + String.valueOf(value1) + " -- " + key2 + ":" + String.valueOf(value2));
        }
    }

    public static void i(int tag, String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        if (tag == 1) {
            takeLog(DC_TAG_1, getClassName(), key1 + ":" + String.valueOf(value1) + " -- " + key2 + ":" + String.valueOf(value2) + " -- " + key3 + ":" + String.valueOf(value3));
        }
    }

    /**
     * 打印的时候带上类名什么的最爽了
     *
     * @return 类名前缀
     */
    private static String getClassName() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        String fileName = traceElement.getFileName();
        //去除文件名中的后缀
        if (fileName.contains(".java")) {
            fileName = fileName.substring(0, fileName.length() - 5);
        }
        return fileName;
    }

    private static String getLineMethod() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        StringBuffer sb = new StringBuffer("[")
                .append(traceElement.getMethodName()).append(" - ")
                .append(traceElement.getLineNumber()).append("]");
        return sb.toString();
    }

    // 获取文件名
    public String getFileName(StackTraceElement traceElement) {
        return traceElement.getFileName();
    }

    // 获取方法名
    public String getMethodName(StackTraceElement traceElement) {
        return traceElement.getMethodName();
    }

    // 获取行数
    public int getLine(StackTraceElement traceElement) {
        return traceElement.getLineNumber();
    }
//    /**
//     * 打印URL的Log
//     *
//     * @param msg       相关信息
//     * @param serverURL 请求地址
//     * @param params    请求队列
//     */
//    public static void url(String msg, String serverURL, RequestParams params) {
//        String url = AsyncHttpClient.getUrlWithQueryString(true, serverURL, params);
//        i("--URL--", msg + "--" + url);//打印
//    }

}
