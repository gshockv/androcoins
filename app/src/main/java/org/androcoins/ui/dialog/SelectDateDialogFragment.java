package org.androcoins.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import org.androcoins.R;
import org.androcoins.ui.dialog.listener.SelectDateListener;

import java.util.Date;

/**
 * Select date dialog implementation with fragment
 * @author vitaly gashock
 */
public class SelectDateDialogFragment extends DialogFragment {
    private SelectDateListener mListener;
    private int mTitle;

    public SelectDateDialogFragment(int title, SelectDateListener listener) {
        mListener = listener;
        mTitle = title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_select_date, null, false);
        ((TextView) root.findViewById(R.id.txt_title)).setText(mTitle);
        bindDialogButtons(root);

        return root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return super.onCreateDialog(savedInstanceState);
    }

    private void bindDialogButtons(final View root) {
        final Button okButton = (Button) root.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePicker picker = (DatePicker) root.findViewById(R.id.date_picker);
                Date date = new Date();
                date.setYear(picker.getYear());
                date.setMonth(picker.getMonth());
                date.setDate(picker.getDayOfMonth());
                mListener.dateSelected(date);
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
