package org.androcoins.ui.dialog;

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
import org.androcoins.ui.dialog.listener.ExpenseMenuListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Context menu dialog implementation with fragments. Used in today expenses activity
 * @author vitaly gashock
 */
public class ExpenseMenuDialogFragment extends DialogFragment {
    private final List<ExpenseMenuItem> mItems = new ArrayList<ExpenseMenuItem>();
    private final ExpenseMenuListener mListener;

    private ExpenseMenuDialogFragment(ExpenseMenuListener listener) {
        mListener = listener;
        mItems.add(new ExpenseMenuItem(AppConstants.Action.ACTION_EDIT, R.string.edit_item));
        mItems.add(new ExpenseMenuItem(AppConstants.Action.ACTION_DELETE, R.string.delete_item));
    }

    public static ExpenseMenuDialogFragment newInstance(ExpenseMenuListener listener) {
        return new ExpenseMenuDialogFragment(listener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.dialog_with_list, container, false);
        final ListView listMenu = (ListView) root.findViewById(R.id.list_container);
        ((TextView) root.findViewById(R.id.txt_title)).setText(R.string.edit_today_ctx_menu_title);
        listMenu.setAdapter(new MenuAdapter(getActivity(), mItems));
        return root;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return super.onCreateDialog(savedInstanceState);
    }

    private class ExpenseMenuItem {
        public int id;
        public int title;

        public ExpenseMenuItem(int id, int title) {
            this.id = id;
            this.title = title;
        }
    }

    private class MenuAdapter extends ArrayAdapter<ExpenseMenuItem> {
        private List<ExpenseMenuItem> mItems;
        private LayoutInflater mInflater;

        public MenuAdapter(Context ctx, List<ExpenseMenuItem> items) {
            super(ctx, R.layout.list_item_menu, items);
            mInflater = LayoutInflater.from(ctx);
            mItems = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ExpenseMenuItem item = mItems.get(position);
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
                    mListener.itemSelected(item.id);
                    dismiss();
                }
            });
            return convertView;
        }

        private class ViewHolder {
            public TextView txtItem;
        }
    }
}
