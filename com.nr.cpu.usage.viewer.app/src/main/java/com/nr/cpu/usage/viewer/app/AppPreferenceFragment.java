package com.nr.cpu.usage.viewer.app;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Preference activity fragment
 */
public class AppPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
