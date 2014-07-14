package org.androcoins.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import org.androcoins.AppConstants;
import org.androcoins.R;
import org.androcoins.data.db.CategoriesDbAdapter;
import org.androcoins.data.db.ExpensesDbAdapter;
import org.androcoins.data.model.CategoryEntity;
import org.androcoins.data.model.ExpenseEntity;
import org.androcoins.ui.dialog.CategorySelectDialogFragment;
import org.androcoins.ui.dialog.EditDialogFragment;
import org.androcoins.ui.dialog.listener.EditDialogListener;
import org.androcoins.ui.dialog.listener.SelectCategoryListener;

import java.util.List;

/**
 * Edit expense item activity
 * @author vitaly gashock
 */
public class EditorActivity extends AppBaseActivity {
    private static final int ACTION_SELECT_CATEGORY_ID = 12;
    private static final int ACTION_SUMMARY_ID = 13;

	private enum Mode {
		NEW_EXPENSE,
		EDIT_EXPENSE
	}
	
	private TextView mExpenseDisplay;
    private Button mButtonOk;
    private Button mButtonDot;
	
	private CategoriesDbAdapter mCategoriesDbAdapter;
	private ExpensesDbAdapter mExpensesDbAdapter;

	private List<CategoryEntity> mActiveCategories;
	private CategoryEntity mCurrentCategory;
    private ExpenseEntity mCurrentExpense;

	private Mode mActivityMode = Mode.NEW_EXPENSE;
	
	private String mCurrentAmount = "0";
	private boolean mDotIsEntered = false;

    private String mSummary;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCategoriesDbAdapter = new CategoriesDbAdapter(this);
        mExpensesDbAdapter = new ExpensesDbAdapter(this);
        
        mExpenseDisplay = (TextView) findViewById(R.id.txt_led);
        mExpenseDisplay.setText(mCurrentAmount);
        mButtonOk = (Button) findViewById(R.id.btn_ok);
        mButtonDot = (Button) findViewById(R.id.btn_dot);
	}

    @Override
    protected void onResume() {
        super.onResume();

        mActiveCategories = mCategoriesDbAdapter.getActiveCategories();

        if (getIntent().getExtras() != null
                && getIntent().getExtras().containsKey(AppConstants.EDIT_EXPENSE_ID_KEY)) {
            mActivityMode = Mode.EDIT_EXPENSE;
            long expenseId = getIntent().getExtras().getLong(AppConstants.EDIT_EXPENSE_ID_KEY);
            fillWidgetsEditMode(expenseId);
        } else {
            mActivityMode = Mode.NEW_EXPENSE;
            applySelectedCategory(mActiveCategories.get(0));    // general category
        }
        changeViewTitle();

        if (amountIsZero()) {
            mButtonOk.setEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(ACTION_BAR_MENU_ID, ACTION_SELECT_CATEGORY_ID, 0, R.string.edit_expense_category_menu)
            .setIcon(R.drawable.ico_action_select)
            .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        SubMenu subMenu = menu.addSubMenu("");
        subMenu.add(ACTION_BAR_SUBMENU_ID, ACTION_SUMMARY_ID, 1, R.string.edit_expense_summary_menu);
        subMenu.getItem()
                .setIcon(com.actionbarsherlock.R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ACTION_SELECT_CATEGORY_ID :
                showSelectCategoryDialog();
                break;
            case ACTION_SUMMARY_ID :
                showSummaryDialog();
                break;
            case android.R.id.home :
                cancelExpenseEditing();
                break;
        }
        return true;
    }

    private void showSelectCategoryDialog() {
        final SelectCategoryListener listener = new SelectCategoryListener() {
            @Override
            public void selected(CategoryEntity category) {
                applySelectedCategory(category);
            }
        };
        final CategorySelectDialogFragment dialog = new CategorySelectDialogFragment(mActiveCategories, listener);
        dialog.show(getSupportFragmentManager(), "select-category-dialog");
    }

    private void showSummaryDialog() {
        final EditDialogListener listener = new EditDialogListener() {
            @Override
            public void edited(String editedValue) {
                mSummary = editedValue;
            }
        };
        final EditDialogFragment dialog = EditDialogFragment.newInstance(mSummary,
                R.string.edit_expense_summary_menu, listener);
        dialog.show(getSupportFragmentManager(), "edit-summary-dialog");
    }

    private void changeViewTitle() {
		switch (mActivityMode) {
			case NEW_EXPENSE :
                getSupportActionBar().setTitle(R.string.add_expense_activity_title);
				break;
			case EDIT_EXPENSE :
                getSupportActionBar().setTitle(R.string.edit_expense_activity_title);
				break;
		}
	}
	
	private void fillWidgetsEditMode(long expenseId) {
		mCurrentExpense = mExpensesDbAdapter.loadEntity(expenseId);
		applySelectedCategory(mCurrentExpense.category);
		mCurrentAmount = String.valueOf(mCurrentExpense.amount);
		displayAmountValue(mCurrentAmount);
        mSummary = mCurrentExpense.summary;
	}
	
	private void applySelectedCategory(final CategoryEntity category) {
		mCurrentCategory = category;
        getSupportActionBar().setSubtitle(getString(R.string.category_subtitle, mCurrentCategory.title));
	}

    public void onDigitClicked(View anchor) {
        String digit = anchor.getTag().toString();
        if (mCurrentAmount.equals("0")) {
            mCurrentAmount = digit;
        } else {
            mCurrentAmount += digit;
        }
        mButtonOk.setEnabled(!amountIsZero());
        displayAmountValue(mCurrentAmount);
    }

    public void onDotClicked(View anchor) {
		if (mDotIsEntered) return;

		mCurrentAmount += ".";
		mExpenseDisplay.setText(mCurrentAmount);

		mDotIsEntered = true;
		mButtonDot.setEnabled(false);
    }

    public void onClearClicked(View anchor) {
		if (mCurrentAmount.equals("0")) {
            mButtonOk.setEnabled(false);
            return;
        }

		if (mCurrentAmount.length() == 1) {
			mCurrentAmount = "0";
			mExpenseDisplay.setText(mCurrentAmount);
            mButtonOk.setEnabled(false);
			return;
		}

		mCurrentAmount = mCurrentAmount.substring(0, mCurrentAmount.length() - 1);
        mDotIsEntered = mCurrentAmount.contains(".");
        mButtonDot.setEnabled(!mDotIsEntered);

		mExpenseDisplay.setText(mCurrentAmount);
        mButtonOk.setEnabled(!amountIsZero());
    }

	private void displayAmountValue(String amount) {
		mExpenseDisplay.setText(amount);
		mDotIsEntered = amount.contains(".");
        mButtonDot.setEnabled(!mDotIsEntered);
	}
	
    public void onOKClicked(View anchor) {
        boolean result = false;
        switch (mActivityMode) {
            case NEW_EXPENSE :
                final ExpenseEntity expense = new ExpenseEntity();
                expense.amount = Double.parseDouble(mCurrentAmount);
                expense.summary = mSummary;
                expense.category = mCurrentCategory;
                result = mExpensesDbAdapter.registerNewEntity(expense) > 0;
                break;
            case EDIT_EXPENSE :
                mCurrentExpense.amount = Double.parseDouble(mCurrentAmount);
                mCurrentExpense.summary = mSummary;
                mCurrentExpense.category = mCurrentCategory;
                result = mExpensesDbAdapter.updateEntity(mCurrentExpense);
                break;
        }
        if (result) {
            setResult(RESULT_OK);
            finish();
        } else {
            onCancelClicked(anchor);
        }
    }

    public void onCancelClicked(View anchor) {
        cancelExpenseEditing();
    }

    private void cancelExpenseEditing() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private boolean amountIsZero() {
        return Double.valueOf(mCurrentAmount) == 0;
    }
}
