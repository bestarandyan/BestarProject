package com.huoqiu.framework.util;

import android.util.Log;

import com.huoqiu.framework.rest.Configuration;

/**
 * Handle Log Print
 * <p/>
 * Created by dench on 2014/11/11.
 * Modify by w_xiong on 2015/01/06 新增兼容爱屋的判断条件
 */
public final class LogUtil {
    LogUtil() {/* Disable Constructor */ }

    public static int v(java.lang.String tag, java.lang.String msg) {
        return Log.v(tag, msg);
    }

    public static int v(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return Log.d(tag, msg, tr);
    }

    public static int d(java.lang.String tag, java.lang.String msg) {
        return Log.d(tag, msg);
    }

    public static int d(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return Log.d(tag, msg, tr);
    }

    public static int i(java.lang.String tag, java.lang.String msg) {
        return Log.i(tag, msg);
    }

    public static int i(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return Log.i(tag, msg, tr);
    }

    public static int w(java.lang.String tag, java.lang.String msg) {
        return Log.w(tag, msg);
    }

    public static int w(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return Log.w(tag, msg, tr);
    }

    public static int w(java.lang.String tag, java.lang.Throwable tr) {
        return Log.w(tag, tr);
    }

    public static int e(java.lang.String tag, java.lang.String msg) {
        return Log.e(tag, msg);
    }

    public static int e(java.lang.String tag, java.lang.String msg, java.lang.Throwable tr) {
        return Log.e(tag, msg, tr);
    }

    public static int println(int priority, java.lang.String tag, java.lang.String msg) {
        return Log.println(priority, tag, msg);
    }
}
