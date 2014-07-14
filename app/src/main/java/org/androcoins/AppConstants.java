package org.androcoins;

/**
 * Commonly used application constants
 * @author vitaly gashock
 */

public final class AppConstants {
    public static final String APP_PACKAGE = "org.androcoins";

	public static final String CURRENCY_PREFS_KEY = "currencyPreference";
	
	public static final String EDIT_EXPENSE_ID_KEY = "editExpenseIdKey";
	public static final int APP_NEW_ITEM_RESULT_CODE = 1001;
	public static final int APP_EDIT_TODAY_RESULT_CODE = 1002;

    public static final String TTF_ROBOTO_REGULAR = "Roboto-Regular.ttf";

    public static class Action {
        public static final int ACTION_INFO = 100500;
        public static final int ACTION_EDIT = 100501;
        public static final int ACTION_DELETE = 100502;
        public static final int ACTION_ACTIVATE = 100503;
        public static final int ACTION_DEACTIVATE = 100504;
    }
}
