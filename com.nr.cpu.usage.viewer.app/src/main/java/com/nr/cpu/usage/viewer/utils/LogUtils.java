package com.nr.cpu.usage.viewer.utils;

import android.content.Context;
import android.util.Log;

import com.nr.cpu.usage.viewer.app.AppConst;

/**
 * Provides convenient methods for application logging
 * using strings defined as string resources
 */
public final class LogUtils {

    private LogUtils(){}

    /**
     * Convenient method for logging errors
     * @param context context to get resource
     * @param resourceId resource id of string
     */
    public static void e(Context context, int resourceId){
        e(context, resourceId);
    }

    /**
     * Convenient method for logging errors
     * @param context context to get resource
     * @param resourceId resource id of formatting string
     * @param args additional message arguments
     */
    public static void e(Context context, int resourceId, Object ... args){
        Log.e(AppConst.LOG_ERROR_ID, String.format(context.getResources().getString(resourceId), args));
    }
}
