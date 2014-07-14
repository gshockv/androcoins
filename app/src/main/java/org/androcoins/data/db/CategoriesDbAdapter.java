package org.androcoins.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.androcoins.R;
import org.androcoins.data.CategoryTable;
import org.androcoins.data.model.CategoryEntity;
import org.androcoins.data.model.CategoryHolder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Database operations with Category items
 * @author vitaly gashock
 */

public final class CategoriesDbAdapter extends DbAdapter<CategoryEntity> {
	public CategoriesDbAdapter(Context ctx) {
		super(ctx);
	}
	
	@Override
	public CategoryEntity loadEntity(long id) {
		final SQLiteDatabase db = mDbHelper.getReadableDatabase();
		final Cursor cursor = db.query(CategoryTable.NAME, CategoryTable.getColumns(),
				DbAdapter.KEY_COLUNM + " = " + id, null, null, null, null);
		
		CategoryEntity category = new CategoryEntity();
		if (cursor.moveToFirst()) {
			category = extractCategory(cursor);
		}
				
		db.close();
		return category;
	}
	
	/**
	 * Get all categories grouped by sections (system and custom)
	 * @return list of grouped categories
	 */
	public List<CategoryHolder> getSectionedCategories() {
        final List<CategoryHolder> categories = new ArrayList<CategoryHolder>();
        final List<CategoryEntity> systemCategories = new ArrayList<CategoryEntity>();
        final List<CategoryEntity> customCategories = new ArrayList<CategoryEntity>();

        final List<CategoryEntity> allCategories = getAllCategories();
        for (CategoryEntity cat : allCategories) {
            if (cat.isCustom == 0) {
                systemCategories.add(cat);
            } else {
                customCategories.add(cat);
            }
        }

        categories.add(new CategoryHolder(R.string.category_system_title, systemCategories));
        categories.add(new CategoryHolder(R.string.category_custom_title, customCategories));
        return categories;
    }

	private List<CategoryEntity> getAllCategories() {
		final SQLiteDatabase db = mDbHelper.getReadableDatabase();
		final Cursor query = db.query(CategoryTable.NAME, CategoryTable.getColumns(), null, null, null, null, null);
		final List<CategoryEntity> categories = extractCategories(query);
		
		db.close();
		return categories;
	}

	/**
	 * Get all active categories
	 * @return list of all active categories
	 */
	public List<CategoryEntity> getActiveCategories() {
		String[] columns = {DbAdapter.KEY_COLUNM, CategoryTable.TITLE_COLUMN, CategoryTable.IS_ACTIVE_COLUNM,
				CategoryTable.IS_CUSTOM_COLUMN, CategoryTable.ICON_ID_COLUMN};
		
		final SQLiteDatabase db = mDbHelper.getReadableDatabase();
		final Cursor cursor = db.query(CategoryTable.NAME, columns, CategoryTable.IS_ACTIVE_COLUNM + " = 1", null, null, null, null);
		final List<CategoryEntity> categories = extractCategories(cursor);
		
		db.close();
		return categories;
	}
	
	// Extract categories list from db Cursor
	private List<CategoryEntity> extractCategories(final Cursor cursor) {
		final List<CategoryEntity> categories = new LinkedList<CategoryEntity>();
		
		if (cursor.moveToFirst()) {
			do {
				categories.add(extractCategory(cursor));
			} while (cursor.moveToNext());
		}
		return categories;
	}
	
	private CategoryEntity extractCategory(final Cursor cursor) {
		long id = cursor.getLong(0);
		String title = cursor.getString(1);
		int isActive = cursor.getInt(2);
		int isCustom = cursor.getInt(3);
		String iconId = cursor.getString(4);
		
		CategoryEntity category = new CategoryEntity();
		category.id = id;
		category.title = title;
		category.isActive = isActive;
		category.isCustom = isCustom;
		category.iconName = iconId;
		
		return category;
	}
}
