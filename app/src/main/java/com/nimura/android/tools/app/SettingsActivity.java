package com.nimura.android.tools.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nimura.android.tools.utils.LogUtils;


/**
 * Settings activity.
 * Implements preference change listener.
 */
public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final CpuUsageController cpuUsageController = CpuUsageController.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key){
            case AppConst.LINE_COLOR_PREFERENCE_ID:
                getLineColorFromPreferences(sharedPreferences);
                break;
            case AppConst.BACKGROUND_COLOR_PREFERENCE_ID:
                getBackgroundColorFromPreferences(sharedPreferences);
                break;
            case AppConst.TEXT_COLOR_PREFERENCE_ID:
                getTextColorFromPreferences(sharedPreferences);
                break;
            case AppConst.UPDATE_INTERVAL_PREFERENCE_ID:
                getUpdateIntervalFromPreferences(sharedPreferences);
                break;
        }
    }

    private void getLineColorFromPreferences(SharedPreferences sharedPreferences) {
        cpuUsageController.setLineColor(sharedPreferences.getInt(AppConst.LINE_COLOR_PREFERENCE_ID, getResources().getColor(R.color.lineColor)));
    }

    private void getBackgroundColorFromPreferences(SharedPreferences sharedPreferences) {
        cpuUsageController.setBackgroundColor(sharedPreferences.getInt(AppConst.BACKGROUND_COLOR_PREFERENCE_ID, getResources().getColor(R.color.backgroundColor)));
    }

    private void getTextColorFromPreferences(SharedPreferences sharedPreferences) {
        cpuUsageController.setTextColor(sharedPreferences.getInt(AppConst.TEXT_COLOR_PREFERENCE_ID, getResources().getColor(R.color.textColor)));
    }

    private void getUpdateIntervalFromPreferences(SharedPreferences sharedPreferences) {
        String txtValue = sharedPreferences.getString(AppConst.UPDATE_INTERVAL_PREFERENCE_ID, getResources().getString(R.string.default_update_interval));
        try {
            cpuUsageController.setUpdateInterval(Integer.parseInt(txtValue));
        }catch(Exception e){
            LogUtils.e(this, R.string.error_update_interval_convertion, txtValue);
        }
    }
}
