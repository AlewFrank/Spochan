package com.android.spochansecondversion;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat { //добавь в папку styles в верхней части строку <item name="preferenceTheme">@style/PreferenceThemeOverlay</item>
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.spochan_preferences);

    }
}
