package com.nimura.android.tools.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


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
                cpuUsageController.setLineColor(sharedPreferences.getInt(AppConst.LINE_COLOR_PREFERENCE_ID, getResources().getColor(R.color.lineColor)));
                break;
            case AppConst.BACKGROUND_COLOR_PREFERENCE_ID:
                cpuUsageController.setBackgroundColor(sharedPreferences.getInt(AppConst.BACKGROUND_COLOR_PREFERENCE_ID, getResources().getColor(R.color.backgroundColor)));
                break;
            case AppConst.TEXT_COLOR_PREFERENCE_ID:
                cpuUsageController.setTextColor(sharedPreferences.getInt(AppConst.TEXT_COLOR_PREFERENCE_ID, getResources().getColor(R.color.textColor)));
                break;
        }
    }
}
