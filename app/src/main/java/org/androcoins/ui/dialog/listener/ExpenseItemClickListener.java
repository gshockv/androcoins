package org.androcoins.ui.dialog.listener;

import org.androcoins.data.model.ExpenseEntity;

/**
 * Expense item click listener
 * @author vitaly gashock
 */
public interface ExpenseItemClickListener {
    public void onItemClicked(ExpenseEntity expense);
    public void onItemContextClicked(ExpenseEntity expense);
}
