package com.newrelic.utils;

import android.os.Looper;

public class Utils {

    private Utils() {
    } // Singelton

    /* General */

    /**
     * Checks if a text is empty or null
     *
     * @param text
     * @return true/false
     */
    public static boolean isEmptyOrNull(String text) {
        return null == text || text.trim().length() == 0;
    }

    /**
     * Checks if the current thread is UI Thread or not.
     *
     * @return
     */
    static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }
}
