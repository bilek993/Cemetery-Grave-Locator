package com.jakubbilinski.cemeterygravelocator;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by bilek on 20.01.2017.
 */

public class PreferencesMainFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_main);
    }
}
