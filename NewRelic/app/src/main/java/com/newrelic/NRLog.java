package com.newrelic;

import android.util.Log;

public class NRLog {
    // Note -
    // keep a WeakReference of loggers here? May be, we don't need it since
    // individual log objects are only held by the owner class and don't leak.

    // logging
    static boolean ENABLE_LOGGING = false;
    static int LOG_LEVEL = Log.ERROR;

    private final String TAG = "NRSdk";
    private final String tag;

    private NRLog(String tag) {
        this.tag = tag;
    }

    public static NRLog getLogger(String tag) {
        return new NRLog(tag);
    }

    protected boolean shouldLog(int level) {
        return (ENABLE_LOGGING && LOG_LEVEL <= level);
    }

    String text(String message) {
        // return String.format("%s/%s - %s", tag, getThreadInfo(), message);
        return String.format("%s [%s] - %s", tag, getThreadInfo(), message);
    }

    public boolean isDebugEnabled() {
        return shouldLog(Log.DEBUG);
    }

    public int debug(String message) {
        return shouldLog(Log.DEBUG) ? Log.d(TAG, text(message)) : 0;
    }

    public int debug(String format, Object... args) {
        return shouldLog(Log.DEBUG) ? Log.d(TAG, text(String.format(format, args))) : 0;
    }

    public int info(String message) {
        return shouldLog(Log.INFO) ? Log.i(TAG, text(message)) : 0;
    }

    public int info(String format, Object... args) {
        return shouldLog(Log.INFO) ? Log.i(TAG, text(String.format(format, args))) : 0;
    }

    public int error(String message) {
        return shouldLog(Log.ERROR) ? Log.e(TAG, text(message)) : 0;
    }

    public int error(String message, Throwable tr) {
        return shouldLog(Log.ERROR) ? Log.e(TAG, text(message), tr) : 0;
    }

    public int error(String format, Object... args) {
        return shouldLog(Log.ERROR) ? Log.e(TAG, text(String.format(format, args))) : 0;
    }

    public static String getThreadInfo() {
        Thread t = Thread.currentThread();
        // return String.format("(%s %d/%d)", t.getName(), t.getId(), t.getPriority());
        return String.format("%s", t.getName());
    }
}
