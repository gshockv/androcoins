package org.androcoins.data.model;

import android.content.ContentValues;
import org.androcoins.data.DbEntity;
import org.androcoins.data.ExpenseTable;
import org.androcoins.data.db.DbAdapter;

import java.text.DateFormat;
import java.util.Date;

/**
 * Represents registered expence item
 * @author vitaly gashock
 */

public class ExpenseEntity implements DbEntity {
	public long id;
	public CategoryEntity category = new CategoryEntity();
	public Date date;
	public double amount;
	public String summary;
	
	public ExpenseEntity() {
		this(0, new Date(), 0, "");
	}
	
	public ExpenseEntity(Date date, double amount) {
		this(0, date, amount);
	}
	
	public ExpenseEntity(long id, Date date, double amount) {
		this(id, date, amount, "");
	}
	
	public ExpenseEntity(long id, double amount, String summary) {
		this(id, new Date(), amount, summary);
	}
	
	public ExpenseEntity(long id, Date date, double amount, String summary) {
		this.id = id;
		this.date = date;
		this.amount = amount;
		this.summary = summary;
	}
	
	@Override
	public ContentValues prepare() {
		final ContentValues cv = new ContentValues();

		if (id != 0) {
			cv.put(DbAdapter.KEY_COLUNM, id);
		}
		cv.put(ExpenseTable.AMOUNT_COLUMN, amount);
		cv.put(ExpenseTable.DATE_COLUMN, DateFormat.getDateInstance(DateFormat.SHORT).format(date));
		cv.put(ExpenseTable.CATEGORY_COLUMN, category.id);
		cv.put(ExpenseTable.SUMMARY_COLUMN, summary);
		
		return cv;
	}

	@Override
	public String getTableName() {
		return ExpenseTable.NAME;
	}

	@Override
	public long getId() {
		return id;
	}
}
