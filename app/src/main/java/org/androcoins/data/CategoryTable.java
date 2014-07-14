package org.androcoins.data;

import android.content.Context;
import org.androcoins.R;
import org.androcoins.data.db.DbAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Expense category table definitions
 * @author vitaly gashock
 */
public final class CategoryTable {
	public static final String NAME = "expense_category";
	
	public static final String TITLE_COLUMN = "title";
	public static final String IS_ACTIVE_COLUNM = "is_active";
	public static final String IS_CUSTOM_COLUMN = "is_custom";
	public static final String ICON_ID_COLUMN = "icon_id";
	
	public static String[] getColumns() {
		return new String[] {
			DbAdapter.KEY_COLUNM,
			CategoryTable.TITLE_COLUMN,
			CategoryTable.IS_ACTIVE_COLUNM,
			CategoryTable.IS_CUSTOM_COLUMN,
			CategoryTable.ICON_ID_COLUMN
		};
	}
	
	public static final String CREATE_TABLE = 
		"CREATE TABLE " + NAME
		+ "(" 
			+ DbAdapter.KEY_COLUNM + " integer primary key autoincrement, "
			+ TITLE_COLUMN + " text not null, "
			+ IS_ACTIVE_COLUNM + " int default 1, "
			+ IS_CUSTOM_COLUMN + " int default 0, "
			+ ICON_ID_COLUMN + " text"
		+ ");";
	
	public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + NAME;
	
	public static List<String> getInitialRecords(Context ctx) {
		final List<String> inserts = new ArrayList<String>();
		
		// All initial predefined categories are active
		
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" + 
				" VALUES('" + ctx.getString(R.string.cat_general) + "', 1, 0, 'ico_general')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" + 
				" VALUES('" + ctx.getString(R.string.cat_transport) + "', 1, 0, 'ico_bus')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_wear) + "', 1, 0, 'ico_tshirt')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_food) + "', 1, 0, 'ico_food')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_shopping) + "', 1, 0, 'ico_cart')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_internet) + "', 1, 0, 'ico_inet')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_cell_phone) + "', 1, 0, 'ico_iphone')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_household) + "', 1, 0, 'ico_house')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_fav) + "', 1, 0, 'ico_heart')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_travel) + "', 1, 0, 'ico_airplane')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_hobby) + "', 1, 0, 'ico_camera')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_private) + "', 1, 0, 'ico_private')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_car) + "', 1, 0, 'ico_car')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_entertainment) + "', 1, 0, 'ico_game')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_pets) + "', 1, 0, 'ico_pets')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_gifts) + "', 1, 0, 'ico_gift')");
		inserts.add("INSERT INTO " + NAME + "(" + TITLE_COLUMN + ", " + IS_ACTIVE_COLUNM + ", " + IS_CUSTOM_COLUMN + ", " + ICON_ID_COLUMN + ")" +
				" VALUES('" + ctx.getString(R.string.cat_child) + "', 1, 0, 'ico_baby')");
		
		return inserts;
	}
}
