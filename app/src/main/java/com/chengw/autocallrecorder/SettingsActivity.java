package com.chengw.autocallrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
    public static final String key_google_drive = "google_drive";

    PreferenceScreen mMainPreferenceScreen;
    PreferenceScreen mGoogleDriverStorage;

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        return false;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();

        if (key.equals(key_google_drive)) {

            // TODO
            Toast.makeText(getApplicationContext(), "TODO google drive", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getBaseContext(), GoogleDriveClient.class);
            startActivity(intent);

            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings_preference);
        
        initial();
    }

    private void initial() {
        mMainPreferenceScreen = this.getPreferenceScreen();

        mGoogleDriverStorage = (PreferenceScreen) mMainPreferenceScreen.findPreference(key_google_drive);
        if (mGoogleDriverStorage != null) {
            mGoogleDriverStorage.setOnPreferenceClickListener(this);
        }
    }
}
