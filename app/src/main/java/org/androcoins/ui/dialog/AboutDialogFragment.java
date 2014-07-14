package org.androcoins.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

/**
 * About dialog implementation via fragments
 * @author vitaly gashock
 */
public class AboutDialogFragment extends DialogFragment {
    private static final String APP_VERSION_KEY = "app.version.params.key";

    private AboutDialogFragment() { }

    public static AboutDialogFragment newInstance(String appVersion) {
        final AboutDialogFragment fragment = new AboutDialogFragment();
        final Bundle params = new Bundle();
        params.putString(APP_VERSION_KEY, appVersion);
        fragment.setArguments(params);
        return fragment;
    }

    @Override
    public View getView() {

        return super.getView();
    }
}
