package org.androcoins.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.androcoins.AppConstants;
import org.androcoins.R;
import org.androcoins.data.model.CategoryEntity;
import org.androcoins.ui.dialog.listener.SelectCategoryListener;

import java.util.List;

/**
 * Select category dialog implementation with fragments API
 * @author vitaly gashock
 */
public class CategorySelectDialogFragment extends DialogFragment {
    private List<CategoryEntity> mCategories;
    private SelectCategoryListener mListener;

    public CategorySelectDialogFragment(List<CategoryEntity> categories, SelectCategoryListener listener) {
        mCategories = categories;
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_with_list, container, false);
        ((TextView) root.findViewById(R.id.txt_title)).setText(R.string.edit_expense_category_menu);

        final ListView listView = (ListView) root.findViewById(R.id.list_container);
        listView.setAdapter(new ListAdapter(getActivity(), mCategories));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mListener.selected(mCategories.get(position));
                dismiss();
            }
        });

        return root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return super.onCreateDialog(savedInstanceState);
    }

    private class ListAdapter extends ArrayAdapter<CategoryEntity> {
        private Context mCtx;
        private List<CategoryEntity> mCategories;

        public ListAdapter(Context ctx, List<CategoryEntity> categories) {
            super(ctx, R.layout.list_item_category_select, categories);
            mCtx = ctx;
            mCategories = categories;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CategoryEntity category = mCategories.get(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mCtx).inflate(R.layout.list_item_category_select, null, false);
                holder = new ViewHolder();
                holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.txtTitle.setText(category.title);
            final Resources res = mCtx.getResources();
            int iconResourceId = res.getIdentifier(category.iconName, "drawable", AppConstants.APP_PACKAGE);
            holder.imgIcon.setImageResource(iconResourceId);

            return convertView;
        }

        private class ViewHolder {
            public ImageView imgIcon;
            public TextView txtTitle;
        }
    }
}
