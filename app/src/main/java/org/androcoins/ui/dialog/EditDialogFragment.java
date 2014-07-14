package org.androcoins.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.androcoins.R;
import org.androcoins.ui.dialog.listener.EditDialogListener;

/**
 * Edit dialog fragment
 * @author vitaly gashock
 */
public class EditDialogFragment extends DialogFragment {
    private EditDialogListener mListener;
    private String mInitialValue;
    private int mDialogTitle;

    private EditDialogFragment(String initialValue, int dialogTitle, EditDialogListener listener) {
        mInitialValue = initialValue;
        mDialogTitle = dialogTitle;
        mListener = listener;
    }

    public static EditDialogFragment newInstance(String initialValue, int dialogTitle, EditDialogListener listener) {
        final EditDialogFragment fragment = new EditDialogFragment(initialValue, dialogTitle, listener);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_edit, container, false);

        final TextView title = (TextView) root.findViewById(R.id.txt_title);
        title.setText(mDialogTitle);
        final EditText editField = (EditText) root.findViewById(R.id.edt_field);

        if (mInitialValue != null) {
            editField.setText(mInitialValue);
        }

        bindDialogButtons(root, editField);

        return root;
    }

    private void bindDialogButtons(View root, final EditText editField) {
        final Button okButton = (Button) root.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = editField.getText().toString();
                if (TextUtils.isEmpty(value)) {
                    return;
                }
                mListener.edited(value);
                dismiss();
            }
        });

        final Button cancelButton = (Button) root.findViewById(R.id.btn_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
