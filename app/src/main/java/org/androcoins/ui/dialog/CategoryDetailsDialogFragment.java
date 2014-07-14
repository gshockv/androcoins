package org.androcoins.ui.dialog;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.androcoins.R;
import org.androcoins.data.model.CategoryDetailsEntity;

/**
 * Category details dialog via fragment
 * @author vitaly gashock
 */
public class CategoryDetailsDialogFragment extends DialogFragment {
    private CategoryDetailsEntity mCategoryDetails;
    private String mCurrency;

    private CategoryDetailsDialogFragment(CategoryDetailsEntity categoryDetails, String currency) {
        mCategoryDetails = categoryDetails;
        mCurrency = currency;
    }

    public static CategoryDetailsDialogFragment newInstance(CategoryDetailsEntity categoryDetails, String currency) {
        final CategoryDetailsDialogFragment fragment = new CategoryDetailsDialogFragment(categoryDetails, currency);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_category_details, container, false);

        Button okButton = (Button) root.findViewById(R.id.btn_ok);
        okButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        initDialogTitle(root);

        final View emptyView = root.findViewById(R.id.txt_empty);
        final View widgetsView = root.findViewById(R.id.view_widgets);

        if (mCategoryDetails.totalExpenses == 0.0) {
            emptyView.setVisibility(View.VISIBLE);
            widgetsView.setVisibility(View.INVISIBLE);
        } else {
            emptyView.setVisibility(View.INVISIBLE);
            widgetsView.setVisibility(View.VISIBLE);
            displayExpences(root);
        }
        return root;
    }

    private void displayExpences(View root) {
        TextView todayExpenses = (TextView) root.findViewById(R.id.category_details_today_text);
        todayExpenses.setText(String.valueOf(mCategoryDetails.todayExpenses));
        TextView weekExpenses = (TextView) root.findViewById(R.id.category_details_week_text);
        weekExpenses.setText(String.valueOf(mCategoryDetails.weekExpenses));
        TextView monthExpenses = (TextView) root.findViewById(R.id.category_details_month_text);
        monthExpenses.setText(String.valueOf(mCategoryDetails.monthExpenses));
        TextView total = (TextView) root.findViewById(R.id.category_details_total_text);
        total.setText(String.valueOf(mCategoryDetails.totalExpenses));

        initCurrencyLabels(root);
    }

    private void initCurrencyLabels(View root) {
        TextView currency_1 = (TextView) root.findViewById(R.id.category_details_currency_1_text);
        TextView currency_2 = (TextView) root.findViewById(R.id.category_details_currency_2_text);
        TextView currency_3 = (TextView) root.findViewById(R.id.category_details_currency_3_text);
        TextView currency_4 = (TextView) root.findViewById(R.id.category_details_currency_4_text);

        currency_1.setText(mCurrency);
        currency_2.setText(mCurrency);
        currency_3.setText(mCurrency);
        currency_4.setText(mCurrency);
    }

    private void initDialogTitle(View root) {
        TextView title = (TextView) root.findViewById(R.id.txt_title);
        title.setText(mCategoryDetails.category.title);
        ImageView icon = (ImageView) root.findViewById(R.id.icon_view);
        Resources res = getActivity().getResources();
        int iconResourceId = res.getIdentifier(mCategoryDetails.category.iconName, "drawable", "org.androcoins");
        icon.setImageResource(iconResourceId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return super.onCreateDialog(savedInstanceState);
    }
}
