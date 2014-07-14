package org.androcoins.data;

import org.androcoins.data.db.DbAdapter;

/**
 * Expense db table definition
 * @author vitaly gashock
 */

public final class ExpenseTable {
	public static final String NAME = "expense";
	
	// columns
	public static final String DATE_COLUMN = "registration_date";
	public static final String CATEGORY_COLUMN = "category_id";
	public static final String AMOUNT_COLUMN = "amount";
	public static final String SUMMARY_COLUMN = "summary";
	
	public static String[] getColumns() {
		return new String[] {
			DbAdapter.KEY_COLUNM,
			ExpenseTable.AMOUNT_COLUMN,
			ExpenseTable.CATEGORY_COLUMN,
			ExpenseTable.SUMMARY_COLUMN,
            ExpenseTable.DATE_COLUMN
		};
	}
	
	public static final String CREATE_TABLE = 
		"CREATE TABLE " + NAME
		+ "("
			+ DbAdapter.KEY_COLUNM + " integer primary key autoincrement, "
			+ DATE_COLUMN + " date not null, "
			+ CATEGORY_COLUMN + " integer not null, "
			+ AMOUNT_COLUMN + " double not null default 0.0, "
			+ SUMMARY_COLUMN + " text"
		+ ")";
	
	public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + NAME;
}
