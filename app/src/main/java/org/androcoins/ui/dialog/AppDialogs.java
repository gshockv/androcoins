package org.androcoins.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.androcoins.R;

/**
 * Application dialogs
 * @author vitaly gashock
 */
public class AppDialogs {
	/**
	 * Display simple text message on popup toast
	 * @param ctx
	 * @param message
	 */
	public static void showToast(Context ctx, String message) {
		Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Application 'About...' dialog
	 * @param context parent context
	 * @param version current version of application
	 * @return dialog instance
	 */
	public static Dialog createAboutDialog(Activity context, String version) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.about_title));
		
		final LayoutInflater inflater = LayoutInflater.from(context);
		final View dialogView = inflater.inflate(R.layout.dialog_about, null);
		final TextView versionText = (TextView) dialogView.findViewById(R.id.about_version_text);
		versionText.setText(version);
		
		builder.setView(dialogView);
		
		builder.setNegativeButton(context.getString(R.string.ok_), null);
		
		return builder.create();
	}

    public static void confirm(FragmentActivity activity, int title, int question, View.OnClickListener okListener) {
        final AlertDialogFragment frag = AlertDialogFragment.newInstance(true);
        frag.setDialogMode(AlertDialogFragment.DialogMode.CONFIRM_DIALOG);
        frag.setTitle(title);
        frag.setMessage(question);
        frag.setPositiveListener(okListener);
        frag.show(activity.getSupportFragmentManager(), "confirm-dialog");
    }
}
