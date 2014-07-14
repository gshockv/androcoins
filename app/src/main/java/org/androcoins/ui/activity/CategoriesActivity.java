package org.androcoins.ui.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.androcoins.AppConstants;
import org.androcoins.R;
import org.androcoins.data.db.CategoriesDbAdapter;
import org.androcoins.data.db.ExpensesDbAdapter;
import org.androcoins.data.model.CategoryDetailsEntity;
import org.androcoins.data.model.CategoryEntity;
import org.androcoins.data.model.CategoryHolder;
import org.androcoins.ui.CategoriesListAdapter;
import org.androcoins.ui.dialog.AppDialogs;
import org.androcoins.ui.dialog.CategoryContextMenuDialogFragment;
import org.androcoins.ui.dialog.CategoryDetailsDialogFragment;
import org.androcoins.ui.dialog.EditDialogFragment;
import org.androcoins.ui.dialog.listener.CategoryMenuListener;
import org.androcoins.ui.dialog.listener.EditDialogListener;

import java.util.List;

/**
 * Categories list activity
 * @author vitaly gashock
 */
public class CategoriesActivity extends AppBaseActivity {
    private static final int CUSTOM_CATEGORIES_POSITION = 1;

	private static final String CUSTOM_CATEGORY_ICON = "ico_custom";
	
	private CategoriesDbAdapter mCategoriesDbAdapter;
	private ExpensesDbAdapter mExpensesDbAdapter;

	private String mUserCurrencyTitle;

    private ListView mListCategories;
    private CategoriesListAdapter mSectionedListAdapter;
    private List<CategoryHolder> mCategories;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_categories);

        mListCategories = (ListView) findViewById(R.id.list_categories);
		mCategoriesDbAdapter = new CategoriesDbAdapter(this);
		mExpensesDbAdapter = new ExpensesDbAdapter(this);
	}

    @Override
    protected void onResume() {
        super.onResume();
		registerForContextMenu(mListCategories);
		loadCategories();
		loadPreferences();
    }

    private void loadPreferences() {
    	final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	mUserCurrencyTitle = prefs.getString(AppConstants.CURRENCY_PREFS_KEY, getResources().getString(R.string.default_currency));
    }

    @Override
    public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
        menu.add(ACTION_BAR_MENU_ID, ACTION_ADD_ID, 0, R.string.action_add)
                .setIcon(R.drawable.ico_action_add)
                .setShowAsAction(com.actionbarsherlock.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case ACTION_ADD_ID :
                requestCategoryCreation();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showContextDialogFor(CategoryEntity category) {
        final CategoryMenuListener callback = new CategoryMenuListener() {
            @Override
            public void itemSelected(int itemId, CategoryEntity selectedCategory) {
                switch (itemId) {
                    case AppConstants.Action.ACTION_INFO :
                        showCategoryDetails(selectedCategory);
                        break;
                    case AppConstants.Action.ACTION_EDIT :
                        tryToEditCategory(selectedCategory);
                        break;
                    case AppConstants.Action.ACTION_DELETE :
                        tryToRemoveCategory(selectedCategory);
                        break;
                    case AppConstants.Action.ACTION_ACTIVATE :
                        performCategoryStateChanging(selectedCategory, true);
                        break;
                    case AppConstants.Action.ACTION_DEACTIVATE :
                        performCategoryStateChanging(selectedCategory, false);
                        break;
                }
            }
        };
        final CategoryContextMenuDialogFragment dialog = CategoryContextMenuDialogFragment.newInstance(category, callback);
        dialog.show(getSupportFragmentManager(), "category-context-dialog");
    }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		final CategoryEntity currentCategory = (CategoryEntity) mSectionedListAdapter.getItem(info.position);
        showContextDialogFor(currentCategory);
	}

	// Reload categories ListView with registered categories
	private void loadCategories() {
        mCategories = mCategoriesDbAdapter.getSectionedCategories();
        mSectionedListAdapter = new CategoriesListAdapter(this, mCategories, mContextClickListener);
        mListCategories.setAdapter(mSectionedListAdapter);
	}
	
	// Register new custom category
	private void addCategory(String title) {
		final CategoryEntity category = new CategoryEntity();
		category.title = title;
		category.isActive = 1;
		category.isCustom = 1;
		category.iconName = CUSTOM_CATEGORY_ICON;
		
		long newlyId = mCategoriesDbAdapter.registerNewEntity(category);
		if (newlyId != -1) {
			category.id = newlyId;
            mCategories.get(CUSTOM_CATEGORIES_POSITION).categories.add(category);
			mSectionedListAdapter.notifyDataSetChanged();
		}
	}
	
	// Edit category in dialog and fix it in local database
	private void tryToEditCategory(final CategoryEntity category) {
        final EditDialogListener callback = new EditDialogListener() {
            @Override
            public void edited(String editedValue) {
                category.title = editedValue;
                boolean result = mCategoriesDbAdapter.updateEntity(category);
                if (result) {
                    loadCategories();
                } else {
                    AppDialogs.showToast(getApplicationContext(), String.format(getString(R.string.category_update_error), category.title));
                }
            }
        };
        final EditDialogFragment dialog = EditDialogFragment.newInstance(category.title,
                R.string.category_dialog_edit_title, callback);
        dialog.show(getSupportFragmentManager(), "category-edit-dialog");
	}
	
	// Remove category
	private void tryToRemoveCategory(final CategoryEntity category) {
        AppDialogs.confirm(this, R.string.category_delete_title,
                R.string.category_delete_confirm,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
				        performCategoryRemoving(category);
                    }
                }
        );
	}
	
	private void performCategoryRemoving(final CategoryEntity category) {
		boolean categoryRemoveResult = mCategoriesDbAdapter.removeEntity(category);
		boolean expensesRemoveResult = mExpensesDbAdapter.removeExpensesForCategory(category.id);
		
		if (categoryRemoveResult) {
            mCategories.get(CUSTOM_CATEGORIES_POSITION).categories.remove(category);
			mSectionedListAdapter.notifyDataSetChanged();
		} else {
			AppDialogs.showToast(getApplicationContext(), String.format(getString(R.string.category_delete_error), category.title));
		}
	}
	
	private void requestCategoryCreation() {
        final EditDialogListener callback = new EditDialogListener() {
            @Override
            public void edited(String editedValue) {
                addCategory(editedValue);
            }
        };
        final EditDialogFragment dialog = EditDialogFragment.newInstance(null,
                R.string.category_dialog_new_title, callback);
        dialog.show(getSupportFragmentManager(), "category-create-dialog");
	}
	
	private void showCategoryDetails(CategoryEntity category) {
        final CategoryDetailsEntity details = mExpensesDbAdapter.getAllExpensesForCategory(category.id);
        details.category = category;
        final CategoryDetailsDialogFragment dialog
                = CategoryDetailsDialogFragment.newInstance(details, mUserCurrencyTitle);
        dialog.show(getSupportFragmentManager(), "category-details-dialog");
	}
	
	private void performCategoryStateChanging(final CategoryEntity category, boolean state) {
		category.isActive = state ? 1 : 0;
		boolean result = mCategoriesDbAdapter.updateEntity(category);
		
		String message;
		if (result) {
			if (state) {
				message = String.format(getString(R.string.category_enabled_message), category.title);
			} else {
				message = String.format(getString(R.string.category_disabled_message), category.title);
			}
		} else {
			message = String.format(getString(R.string.category_update_error), category.title);
		}
		AppDialogs.showToast(getApplicationContext(), message);
        mSectionedListAdapter.notifyDataSetChanged();
	}

	private View.OnClickListener mContextClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openContextMenu(view);
        }
    };
}
