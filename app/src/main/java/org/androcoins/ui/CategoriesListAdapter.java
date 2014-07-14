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
import org.androcoins.data.model.CategoryEntity;
import org.androcoins.data.model.CategoryHolder;

import java.util.List;

/**
 * Categories list sectioned adapter
 * @author vitaly gashock
 */
public class CategoriesListAdapter extends BaseAdapter {
    private static final int SECTION_ITEM_VIEW_TYPE = 0;
    private static final int CATEGORY_ITEM_VIEW_TYPE = 1;
    private static final int VIEWS_COUNT = 2;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CategoryHolder> mHolders;

    private String mActivatedStateString;
    private String mDisabledStateString;

    private View.OnClickListener mContextClickListener;

    public CategoriesListAdapter(Context ctx,
                                 List<CategoryHolder> holders,
                                 View.OnClickListener contextListener) {
        mContext = ctx;
        mInflater = LayoutInflater.from(ctx);
        mHolders = holders;
        mContextClickListener = contextListener;
        this.mActivatedStateString = mContext.getString(R.string.category_enabled_state);
        this.mDisabledStateString = mContext.getString(R.string.category_disabled_state);
    }

    @Override
    public int getCount() {
        int count = mHolders.size();
        for (CategoryHolder holder : mHolders) {
            count += holder.categories.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        if (position < 0) throw new IllegalArgumentException("Position must be great than zero");

        int itemsLeft = position;
        for (CategoryHolder holder : mHolders) {
            if (itemsLeft == 0) {
                return holder;
            }
            itemsLeft--;
            if (itemsLeft < holder.categories.size()) {
                for (CategoryEntity category : holder.categories) {
                    if (itemsLeft == 0) {
                        return category;
                    }
                    itemsLeft--;
                }
            } else {
                itemsLeft -= holder.categories.size();
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int itemsLeft = position;
        for (CategoryHolder holder : mHolders) {
        	if (holder.categories.isEmpty()) {
        		continue;
        	}
            if (itemsLeft == 0) {
                return SECTION_ITEM_VIEW_TYPE;
            }
            itemsLeft--;
            if (itemsLeft < holder.categories.size()) {
                return CATEGORY_ITEM_VIEW_TYPE;
            }
            itemsLeft -= holder.categories.size();
        }
        return IGNORE_ITEM_VIEW_TYPE;
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
            final CategoryHolder holder = (CategoryHolder) getItem(position);
            convertView = mInflater.inflate(R.layout.list_item_section_view, parent, false);
            ((TextView) convertView.findViewById(R.id.txt_title)).setText(holder.title);
        } else if (viewType == CATEGORY_ITEM_VIEW_TYPE) {
            final CategoryEntity category = (CategoryEntity) getItem(position);
            CategoryViewHolder holder;
            if (convertView == null) {
                holder = new CategoryViewHolder();
                convertView = mInflater.inflate(R.layout.list_item_iconified, parent, false);
                holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_icon);
                holder.txtTitle = (TextView) convertView.findViewById(R.id.txt_title);
                holder.txtSubtitle = (TextView) convertView.findViewById(R.id.txt_subtitle);
                holder.txtContext = (TextView) convertView.findViewById(R.id.txt_context);
                convertView.setTag(holder);
            } else {
                holder = (CategoryViewHolder) convertView.getTag();
            }

            final Resources res = mContext.getResources();
            int iconResourceId = res.getIdentifier(category.iconName, "drawable", AppConstants.APP_PACKAGE);
            holder.imgIcon.setImageResource(iconResourceId);
            holder.txtTitle.setText(category.title);
            holder.txtSubtitle.setText(category.isActive == 1 ? mActivatedStateString : mDisabledStateString);
            holder.txtContext.setOnClickListener(mContextClickListener);
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return VIEWS_COUNT;
    }

    private class CategoryViewHolder {
        public ImageView imgIcon;
        public TextView txtTitle;
        public TextView txtSubtitle;
        public TextView txtContext;
    }
}
