package org.androcoins.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import org.androcoins.data.DbEntity;
import org.androcoins.data.SqliteDbHelper;

/**
 * All database related operations lives here
 * @author vitaly gashock
 */

public abstract class DbAdapter<T extends DbEntity> {
	private static final String DB_NAME = "androcoins.db";
	private static final int DB_VERSION = 1;
	
	public static final String KEY_COLUNM = "id";	
	
	protected SqliteDbHelper mDbHelper;
	
	public DbAdapter(Context ctx) {
		mDbHelper = new SqliteDbHelper(ctx, DB_NAME, DB_VERSION);
	}
	
	/**
	 * Load entity by id
	 * @param id entity's id
	 * @return loaded entity object
	 */
	public abstract T loadEntity(long id);
	
	/**
	 * Add new database entity
	 * @param entity entity
	 * @return id of inserted record
	 */
	public long registerNewEntity(final T entity) {
		final SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long result = db.insert(entity.getTableName(), null, entity.prepare());
		db.close();
		return result;
	}
	
	/**
	 * Update database entity
	 * @param entity entity
	 * @return true if update completed
	 */
	public boolean updateEntity(final T entity) {
		final SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int result = db.update(entity.getTableName(), entity.prepare(), 
				DbAdapter.KEY_COLUNM + " = " + entity.getId(), null);
		db.close();
		return result > 0;
	}
	
	/**
	 * Remove an database entity
	 * @param entity db entity
	 * @return true if removing successed
	 */
	public boolean removeEntity(final T entity) {
		final SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int result = db.delete(entity.getTableName(), DbAdapter.KEY_COLUNM + " = " + entity.getId(), null);
		db.close();
		return result > 0;
	}
}
