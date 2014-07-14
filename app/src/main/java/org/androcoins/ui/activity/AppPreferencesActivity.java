package org.androcoins.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import org.androcoins.R;
import org.androcoins.ui.dialog.AppDialogs;

/**
 * Application preferences activity
 * @author vitaly gashock
 */
public class AppPreferencesActivity extends SherlockPreferenceActivity {
    private String mAppVersion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        final Preference aboutPrefs = (Preference) findPreference("aboutPrefs");
        if (aboutPrefs != null) {
            aboutPrefs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    AppDialogs.createAboutDialog(AppPreferencesActivity.this, mAppVersion).show();
                    return true;
                }
            });
        }

        try {
            mAppVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            mAppVersion = "Unreachable";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
