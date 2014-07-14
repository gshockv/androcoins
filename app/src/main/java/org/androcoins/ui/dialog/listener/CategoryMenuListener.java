package org.androcoins.ui.dialog.listener;

import org.androcoins.data.model.CategoryEntity;

/**
 * Category menu callback
 * @author vitaly gashock
 */
public interface CategoryMenuListener {
    public void itemSelected(int itemId, CategoryEntity category);
}
