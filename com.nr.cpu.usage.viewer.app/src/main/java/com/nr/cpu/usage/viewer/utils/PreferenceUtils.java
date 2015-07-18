package com.nr.cpu.usage.viewer.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.nr.cpu.usage.viewer.app.AppConst;
import com.nr.cpu.usage.viewer.app.CpuUsageController;
import com.nr.cpu.usage.viewer.app.R;

/**
 * Preference utils
 */
public final class PreferenceUtils {
    private PreferenceUtils(){}

    /**
     * Updates line color from preferences
     * @param context context
     * @param sharedPreferences preferences
     */
    public static void getLineColorFromPreferences(Context context, SharedPreferences sharedPreferences) {
        CpuUsageController.getInstance().setLineColor(
                sharedPreferences.getInt(AppConst.LINE_COLOR_PREFERENCE_ID, context.getResources().getColor(R.color.lineColor)));
    }

    /**
     * Updates mesh color from preferences
     * @param context context
     * @param sharedPreferences preferences
     */
    public static void getMeshColorFromPreferences(Context context, SharedPreferences sharedPreferences) {
        CpuUsageController.getInstance().setMeshColor(
                sharedPreferences.getInt(AppConst.MESH_COLOR_PREFERENCE_ID, context.getResources().getColor(R.color.meshColor)));
    }

    /**
     * Updates background color from preferences
     * @param context context
     * @param sharedPreferences preferences
     */
    public static void getBackgroundColorFromPreferences(Context context, SharedPreferences sharedPreferences) {
        CpuUsageController.getInstance().setBackgroundColor(
                sharedPreferences.getInt(AppConst.BACKGROUND_COLOR_PREFERENCE_ID, context.getResources().getColor(R.color.backgroundColor)));
    }

    /**
     * Updates text color from preferences
     * @param context context
     * @param sharedPreferences preferences
     */
    public static void getTextColorFromPreferences(Context context, SharedPreferences sharedPreferences) {
        CpuUsageController.getInstance().setTextColor(
                sharedPreferences.getInt(AppConst.TEXT_COLOR_PREFERENCE_ID, context.getResources().getColor(R.color.textColor)));
    }

    /**
     * Updates update interval from preferences
     * @param context context
     * @param sharedPreferences preferences
     */
    public static void getUpdateIntervalFromPreferences(Context context, SharedPreferences sharedPreferences) {
        String txtValue = sharedPreferences.getString(AppConst.UPDATE_INTERVAL_PREFERENCE_ID, context.getResources().getString(R.string.default_update_interval));
        try {
            CpuUsageController.getInstance().setUpdateInterval(Integer.parseInt(txtValue));
        }catch(Exception e){
            LogUtils.e(context, R.string.error_update_interval_convertion, txtValue);
        }
    }

    /**
     * Updates draw mesh flag from preferences
     * @param context context
     * @param sharedPreferences preferences
     */
    public static void getDrawMeshFlag(Context context, SharedPreferences sharedPreferences) {
        CpuUsageController.getInstance().setDrawMesh(sharedPreferences.getBoolean(AppConst.DRAW_MESH_PREFERENCE_ID, true));
    }
}
