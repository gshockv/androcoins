package org.androcoins.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import org.androcoins.R;

/**
 * AlertDialog implementation with fragments
 * @author vitaly gashock
 */
public final class AlertDialogFragment extends DialogFragment {

    public enum DialogMode {
        ALERT_DIALOG,
        CONFIRM_DIALOG
    }

    private AlertDialogFragment() { }

    private static final String ARG_TITLE_ID = "arg_title_id";
    private static final String ARG_TITLE_STRING = "arg_title_string";
    private static final String ARG_MESSAGE_ID = "arg_message_id";
    private static final String ARG_MESSAGE_STRING = "arg_message_string";
    private static final String ARG_CANCELABLE = "arg_cancelable";

    private static View.OnClickListener mPositiveListener;
    private static View.OnClickListener mNegativeListener;

    private static DialogMode mDialogMode = DialogMode.ALERT_DIALOG;

    /**
     * Create new fragment instance
     *
     * @param cancelable if <code>true</code> then dialog can be canceled, <code>false</code> otherwise
     * @return instantiated dialog fragment
     */
    public static AlertDialogFragment newInstance(boolean cancelable) {
        final AlertDialogFragment fragment = new AlertDialogFragment();
        final Bundle args = new Bundle();
        args.putBoolean(ARG_CANCELABLE, cancelable);
        fragment.setArguments(args);
        return fragment;
    }

    public void setTitle(String title) {
        final Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("Fragment does not contain bundled arguments! " +
                    "Create fragment with newInstance method");
        }
        if (args.containsKey(ARG_TITLE_ID)) {
            args.remove(ARG_TITLE_ID);
        }
        args.putString(ARG_TITLE_STRING, title);
        this.setArguments(args);
    }

    public void setTitle(int title) {
        final Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("Fragment does not contain bundled arguments! " +
                    "Create fragment with newInstance method");
        }
        if (args.containsKey(ARG_TITLE_STRING)) {
            args.remove(ARG_TITLE_STRING);
        }
        args.putInt(ARG_TITLE_ID, title);
        this.setArguments(args);
    }

    public void setMessage(String message) {
        final Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("Fragment does not contain bundled arguments! " +
                    "Create fragment with newInstance method");
        }
        if (args.containsKey(ARG_MESSAGE_ID)) {
            args.remove(ARG_MESSAGE_ID);
        }
        args.putString(ARG_MESSAGE_STRING, message);
        this.setArguments(args);
    }

    public void setMessage(int message) {
        final Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("Fragment does not contain bundled arguments! " +
                    "Create fragment with newInstance method");
        }
        if (args.containsKey(ARG_MESSAGE_STRING)) {
            args.remove(ARG_MESSAGE_STRING);
        }
        args.putInt(ARG_MESSAGE_ID, message);
        this.setArguments(args);
    }

    public void setPositiveListener(View.OnClickListener listener) {
        mPositiveListener = listener;
    }

    public void setNegativeListener(View.OnClickListener listener) {
        mNegativeListener = listener;
    }

    public void setDialogMode(DialogMode mode) {
        mDialogMode = mode;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(getArguments().getBoolean(ARG_CANCELABLE));
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("Fragment does not contain bundled arguments! " +
                    "Create fragment with newInstance method");
        }

        final View view = inflater.inflate(R.layout.dialog_alert, container, false);

        final TextView titleView = (TextView) view.findViewById(R.id.txt_title);
        if (args.containsKey(ARG_TITLE_ID)) {
            titleView.setText(args.getInt(ARG_TITLE_ID));
        } else if (args.containsKey(ARG_TITLE_STRING)) {
            titleView.setText(args.getString(ARG_TITLE_STRING));
        } else {
            titleView.setText("");
        }

        final TextView messageView = (TextView) view.findViewById(R.id.txt_message);
        if (args.containsKey(ARG_MESSAGE_ID)) {
            messageView.setText(args.getInt(ARG_MESSAGE_ID));
        } else if (args.containsKey(ARG_MESSAGE_STRING)) {
            messageView.setText(args.getString(ARG_MESSAGE_STRING));
        }

        final Button btnOk = (Button) view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mPositiveListener != null) {
                    mPositiveListener.onClick(v);
                }
            }
        });

        final View separatorView = view.findViewById(R.id.buttons_separator_view);
        final Button btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        if (mDialogMode == DialogMode.CONFIRM_DIALOG) {
            // turn on Cancel button
            separatorView.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if (mNegativeListener != null) {
                        mNegativeListener.onClick(v);
                    }
                }
            });
        } else {
            separatorView.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        }

        return view;
    }
}
