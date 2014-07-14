package org.androcoins.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import org.androcoins.AppConstants;
import org.androcoins.R;
import org.androcoins.data.db.ExpensesDbAdapter;
import org.androcoins.data.model.ExpenseEntity;
import org.androcoins.data.model.ExpenseHolder;
import org.androcoins.ui.ExpensesListAdapter;
import org.androcoins.ui.dialog.AppDialogs;
import org.androcoins.ui.dialog.ExpenseMenuDialogFragment;
import org.androcoins.ui.dialog.listener.ExpenseItemClickListener;
import org.androcoins.ui.dialog.listener.ExpenseMenuListener;
import org.androcoins.util.AppUtils;

import java.util.List;

/**
 * Today expenses activity
 * @author vitaly gashock
 */
public class TodayActivity extends AppBaseActivity {
    private static final int ACTION_SETTINGS_ID = 12;

	private TextView mTodayAmoutView;
    private Button mNewExpenseButton;
    private ListView mExpensesList;

	private ExpensesDbAdapter mExpensesDbAdapter;
    private ExpensesListAdapter mListAdapter;
    private List<ExpenseHolder> mExpenses;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_today);

        mTodayAmoutView = (TextView) findViewById(R.id.today_expenses_text);
        mNewExpenseButton = (Button) findViewById(R.id.btn_new);
        mExpensesList = (ListView) findViewById(R.id.list_expenses);
        registerForContextMenu(mExpensesList);

        mExpensesDbAdapter = new ExpensesDbAdapter(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        reloadCurrentExpenses();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(ACTION_BAR_MENU_ID, ACTION_ADD_ID, 0, R.string.action_add)
                .setIcon(R.drawable.ico_action_add)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        SubMenu subMenu = menu.addSubMenu("");
        subMenu.add(ACTION_BAR_SUBMENU_ID, ACTION_SETTINGS_ID, 1, R.string.action_settings);
        subMenu.getItem()
                .setIcon(com.actionbarsherlock.R.drawable.abs__ic_menu_moreoverflow_normal_holo_dark)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case ACTION_ADD_ID :
				onNewExpenseClick(item.getActionView());
				return true;
			case ACTION_SETTINGS_ID :
                onSettingsClick(item.getActionView());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final ExpenseEntity expense = (ExpenseEntity) mListAdapter.getItem(info.position);
        showContextDialogFor(expense);
    }

    private void reloadCurrentExpenses() {
        double expensesAmount = mExpensesDbAdapter.getTodayRegisteredAmounts();
        mTodayAmoutView.setText(AppUtils.formatToCurrency(expensesAmount));

        mExpenses = mExpensesDbAdapter.getTodayExpenseHolders();
        if (isExpensesEmpty()) {
            mExpensesList.setVisibility(View.GONE);
            mNewExpenseButton.setVisibility(View.VISIBLE);
        } else {
            mListAdapter = new ExpensesListAdapter(this, mExpenses, mExpenseCLickListener);
            mExpensesList.setAdapter(mListAdapter);
            mNewExpenseButton.setVisibility(View.GONE);
            mExpensesList.setVisibility(View.VISIBLE);
        }
    }

    private ExpenseItemClickListener mExpenseCLickListener = new ExpenseItemClickListener() {
        @Override
        public void onItemClicked(ExpenseEntity expense) {
            requestEditExpenseViewOpening(expense);
        }

        @Override
        public void onItemContextClicked(ExpenseEntity expense) {
            showContextDialogFor(expense);
        }
    };

    private void requestEditExpenseViewOpening(ExpenseEntity expense) {
        final Intent editExpenseIntent = new Intent(getApplicationContext(), EditorActivity.class);
        editExpenseIntent.putExtra(AppConstants.EDIT_EXPENSE_ID_KEY, expense.id);
        startActivityForResult(editExpenseIntent, AppConstants.APP_EDIT_TODAY_RESULT_CODE);
    }

    private void tryToRemoveExpenseItem(final ExpenseEntity expense) {
        AppDialogs.confirm(this, R.string.remove_today_item_title,
                R.string.remove_today_item_confirm,
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        boolean result = mExpensesDbAdapter.removeEntity(expense);
                        if (result) {
                            reloadCurrentExpenses();
                        }
                    }
                }
        );
    }

    private void showContextDialogFor(final ExpenseEntity expense) {
        final ExpenseMenuListener listener = new ExpenseMenuListener() {
            @Override
            public void itemSelected(int id) {
                if (id == AppConstants.Action.ACTION_EDIT) {
                    requestEditExpenseViewOpening(expense);
                } else if (id == AppConstants.Action.ACTION_DELETE) {
                    tryToRemoveExpenseItem(expense);
                }
            }
        };
        final ExpenseMenuDialogFragment dialog = ExpenseMenuDialogFragment.newInstance(listener);
        dialog.show(getSupportFragmentManager(), "expense-context-dialog");
    }

	public void onNewExpenseClick(View anchor) {
		final Intent newExpenseIntent = new Intent(getApplicationContext(), EditorActivity.class);
		startActivityForResult(newExpenseIntent, AppConstants.APP_NEW_ITEM_RESULT_CODE);
	}

    private boolean isExpensesEmpty() {
        boolean empty = mExpenses.isEmpty();

        if (mExpenses.size() == 1) {
            empty = mExpenses.get(0).expenses.isEmpty();
        }

        return empty;
    }
}
