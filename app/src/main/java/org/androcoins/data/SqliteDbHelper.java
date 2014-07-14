package org.androcoins.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLite database internal helper.
 * Opens, creates and updates internal SQLite database
 * 
 * @author vitaly gashock
 */

public final class SqliteDbHelper extends SQLiteOpenHelper {
	private Context mContext;
	
	public SqliteDbHelper(Context ctx, String dbName, int dbVersion) {
		super(ctx, dbName, null, dbVersion);
		this.mContext = ctx;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// categories dict
		db.execSQL(CategoryTable.CREATE_TABLE);
		for (String insert : CategoryTable.getInitialRecords(mContext)) {
			db.execSQL(insert);
		}
		// expenses
		db.execSQL(ExpenseTable.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(ExpenseTable.DELETE_TABLE);
		db.execSQL(CategoryTable.DELETE_TABLE);
		onCreate(db);
	}
}
