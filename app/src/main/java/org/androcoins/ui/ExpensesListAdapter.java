package org.androcoins.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.androcoins.AppConstants;
import org.androcoins.R;
import org.androcoins.data.model.ExpenseEntity;
import org.androcoins.data.model.ExpenseHolder;
import org.androcoins.ui.dialog.listener.ExpenseItemClickListener;
import org.androcoins.util.AppUtils;

import java.text.DateFormat;
import java.util.List;

/**
 * @author vitaly gashock
 */
public class ExpensesListAdapter extends BaseAdapter {
    private static final int SECTION_ITEM_VIEW_TYPE = 0;
    private static final int EXPENSE_ITEM_VIEW_TYPE = 1;
    private static final int VIEWS_COUNT = 2;

    private Context mContext;
    private List<ExpenseHolder> mExpenseHolders;

    private final LayoutInflater mInflater;
    private final ExpenseItemClickListener mClickListener;

    public ExpensesListAdapter(Context ctx, List<ExpenseHolder> expensesHolders,
                               ExpenseItemClickListener clickListener) {
        mContext = ctx;
        mExpenseHolders = expensesHolders;
        mClickListener = clickListener;
        mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        if (mExpenseHolders.size() == 1) {
            return mExpenseHolders.get(0).expenses.size();
        }
        int count = mExpenseHolders.size();
        for (ExpenseHolder holder : mExpenseHolders) {
            count += holder.expenses.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if (position < 0) throw new IllegalArgumentException("Position must be great than zero");

        if (mExpenseHolders.size() == 1) {
            return mExpenseHolders.get(0).expenses.get(position);
        }

        int itemsLeft = position;
        for (ExpenseHolder holder : mExpenseHolders) {
            if (itemsLeft == 0) {
                return holder;
            }
            itemsLeft--;
            if (itemsLeft < holder.expenses.size()) {
                for (ExpenseEntity expense : holder.expenses) {
                    if (itemsLeft == 0) {
                        return expense;
                    }
                    itemsLeft--;
                }
            } else {
                itemsLeft -= holder.expenses.size();
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (mExpenseHolders.size() == 1) {
            return EXPENSE_ITEM_VIEW_TYPE;
        }

        int itemsLeft = position;
        for (ExpenseHolder holder : mExpenseHolders) {
            if (holder.expenses.isEmpty()) {
                continue;
            }
            if (itemsLeft == 0) {
                return SECTION_ITEM_VIEW_TYPE;
            }
            itemsLeft--;
            if (itemsLeft < holder.expenses.size()) {
                return EXPENSE_ITEM_VIEW_TYPE;
            }
            itemsLeft -= holder.expenses.size();
        }

        return IGNORE_ITEM_VIEW_TYPE;
    }

    @Override
    public int getViewTypeCount() {
        return VIEWS_COUNT;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int viewType = getItemViewType(position);
        if (viewType == IGNORE_ITEM_VIEW_TYPE) {
            final View empty = new View(mContext);
            return empty;
        }

        if (viewType == SECTION_ITEM_VIEW_TYPE) {
            // title
            final ExpenseHolder holder = (ExpenseHolder) getItem(position);
            if (holder.date != null) {
                convertView = mInflater.inflate(R.layout.list_item_section_view, parent, false);
                String text = DateFormat.getDateInstance(DateFormat.LONG).format(holder.date);
                ((TextView) convertView.findViewById(R.id.txt_title)).setText(text);
            }
        } else if (viewType == EXPENSE_ITEM_VIEW_TYPE) {
            final ExpenseEntity expense = (ExpenseEntity) getItem(position);
            ItemViewHolder viewHolder = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_iconified, parent, false);

                viewHolder = new ItemViewHolder();
                viewHolder.iconView = (ImageView) convertView.findViewById(R.id.img_icon);
                viewHolder.amountView = (TextView) convertView.findViewById(R.id.txt_title);
                viewHolder.subtitleView = (TextView) convertView.findViewById(R.id.txt_subtitle);
                viewHolder.contextView = (TextView) convertView.findViewById(R.id.txt_context);

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ItemViewHolder) convertView.getTag();
            }

            // load icon resource by saved name
            Resources res = mContext.getResources();
            int iconResourceId = res.getIdentifier(expense.category.iconName, "drawable", AppConstants.APP_PACKAGE);
            viewHolder.iconView.setImageResource(iconResourceId);

            String summaryText = expense.summary == null ? "" : " - " + expense.summary;
            viewHolder.subtitleView.setText(
                    mContext.getString(R.string.category_with_summary, expense.category.title, summaryText));
            viewHolder.amountView.setText(AppUtils.formatToCurrency(expense.amount));

            viewHolder.contextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemContextClicked(expense);
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onItemClicked(expense);
                }
            });
        }

        return convertView;
    }

    private class ItemViewHolder {
        public ImageView iconView;
        public TextView subtitleView;
        public TextView amountView;
        public TextView contextView;
    }
}
