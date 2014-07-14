package org.androcoins.data.model;

import java.util.Date;
import java.util.List;

/**
 * Needed for sectioning expenses list
 * @author vitaly gashock
 */
public class ExpenseHolder {
    public Date date;
    public List<ExpenseEntity> expenses;

    public ExpenseHolder(Date date, List<ExpenseEntity> expenses) {
        this.date = date;
        this.expenses = expenses;
    }
}
