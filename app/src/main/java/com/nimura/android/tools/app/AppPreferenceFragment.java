package com.nimura.android.tools.app;

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
