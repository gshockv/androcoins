package org.androcoins.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.androcoins.AppConstants;
import org.androcoins.R;
import org.androcoins.data.model.CategoryEntity;
import org.androcoins.ui.dialog.listener.CategoryMenuListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Category context menu dialog implementation via fragments API
 * @author vitaly gashock
 */
public class CategoryContextMenuDialogFragment extends DialogFragment {
    private List<CategoryMenuItem> mItems = new ArrayList<CategoryMenuItem>();

    private CategoryEntity mCategory;
    private CategoryMenuListener mCallback;

    private CategoryContextMenuDialogFragment(CategoryEntity category, CategoryMenuListener callback) {
        mCategory = category;
        mCallback = callback;
    }

    public static CategoryContextMenuDialogFragment newInstance(CategoryEntity category, CategoryMenuListener callback) {
        final CategoryContextMenuDialogFragment fragment = new CategoryContextMenuDialogFragment(category, callback);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (mCategory.title.equals(getResources().getString(R.string.cat_general))) {
            mItems.add(new CategoryMenuItem(AppConstants.Action.ACTION_INFO, R.string.category_info_item));
            return;
        }

        if (mCategory.isActive == 1) {
            mItems.add(new CategoryMenuItem(AppConstants.Action.ACTION_DEACTIVATE, R.string.category_do_disactivate));
        } else {
            mItems.add(new CategoryMenuItem(AppConstants.Action.ACTION_ACTIVATE, R.string.category_do_activate));
        }
        if (mCategory.isCustom == 1) {
            // build menu for custom category
            mItems.add(new CategoryMenuItem(AppConstants.Action.ACTION_INFO, R.string.category_info_item));
            mItems.add(new CategoryMenuItem(AppConstants.Action.ACTION_EDIT, R.string.edit_item));
            mItems.add(new CategoryMenuItem(AppConstants.Action.ACTION_DELETE, R.string.delete_item));
        } else {
            // build menu for system category
            mItems.add(new CategoryMenuItem(AppConstants.Action.ACTION_INFO, R.string.category_info_item));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_with_list, container, false);
        final ListView listMenu = (ListView) root.findViewById(R.id.list_container);
        listMenu.setAdapter(new MenuListAdapter(getActivity(), mItems));
        return root;
    }

    private class CategoryMenuItem {
        public int id;
        public int title;

        public CategoryMenuItem(int id, int title) {
            this.id = id;
            this.title = title;
        }
    }

    private class MenuListAdapter extends ArrayAdapter<CategoryMenuItem> {
        private List<CategoryMenuItem> mItems;
        private LayoutInflater mInflater;

        public MenuListAdapter(Context ctx, List<CategoryMenuItem> items) {
            super(ctx, R.layout.list_item_menu, items);
            mItems = items;
            mInflater = LayoutInflater.from(ctx);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final CategoryMenuItem item = mItems.get(position);
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item_menu, null, false);
                holder.txtItem = (TextView) convertView.findViewById(R.id.txt_item);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.txtItem.setText(item.title);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.itemSelected(item.id, mCategory);
                    getDialog().dismiss();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            public TextView txtItem;
        }
    }
}
