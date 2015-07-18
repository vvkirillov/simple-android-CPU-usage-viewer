package com.nr.cpu.usage.viewer.app;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nr.cpu.usage.viewer.utils.PreferenceUtils;


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
                PreferenceUtils.getLineColorFromPreferences(this, sharedPreferences);
                break;
            case AppConst.MESH_COLOR_PREFERENCE_ID:
                PreferenceUtils.getMeshColorFromPreferences(this, sharedPreferences);
                break;
            case AppConst.BACKGROUND_COLOR_PREFERENCE_ID:
                PreferenceUtils.getBackgroundColorFromPreferences(this, sharedPreferences);
                break;
            case AppConst.TEXT_COLOR_PREFERENCE_ID:
                PreferenceUtils.getTextColorFromPreferences(this, sharedPreferences);
                break;
            case AppConst.UPDATE_INTERVAL_PREFERENCE_ID:
                PreferenceUtils.getUpdateIntervalFromPreferences(this, sharedPreferences);
                break;
            case AppConst.DRAW_MESH_PREFERENCE_ID:
                PreferenceUtils.getDrawMeshFlag(this, sharedPreferences);
                break;
        }
    }
}
