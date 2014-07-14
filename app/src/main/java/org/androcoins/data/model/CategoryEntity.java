package org.androcoins.data.model;

import android.content.ContentValues;
import org.androcoins.data.CategoryTable;
import org.androcoins.data.DbEntity;
import org.androcoins.data.db.DbAdapter;

/**
 * Represents expense category
 * @author vitaly gashock
 */
public class CategoryEntity implements DbEntity {
	public long id = 0;
	public String title;
	public int isActive;
	public long isCustom;
	public String iconName;	
	
	@Override
	public ContentValues prepare() {
		final ContentValues vals = new ContentValues();
		if (id != 0) {
			vals.put(DbAdapter.KEY_COLUNM, id);
		}
		vals.put(CategoryTable.TITLE_COLUMN, title);
		vals.put(CategoryTable.IS_ACTIVE_COLUNM, isActive);
		vals.put(CategoryTable.IS_CUSTOM_COLUMN, isCustom);
		vals.put(CategoryTable.ICON_ID_COLUMN, iconName);
		return vals;
	}

	@Override
	public String getTableName() {
		return CategoryTable.NAME;
	}

	@Override
	public long getId() {
		return id;
	}
}
