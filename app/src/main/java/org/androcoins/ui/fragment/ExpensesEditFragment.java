package org.androcoins.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import org.androcoins.AppConstants;
import org.androcoins.R;
import org.androcoins.data.db.ExpensesDbAdapter;
import org.androcoins.data.model.ExpenseEntity;
import org.androcoins.data.model.ExpenseHolder;
import org.androcoins.ui.ExpensesEditPagerScreen;
import org.androcoins.ui.ExpensesListAdapter;
import org.androcoins.ui.activity.EditorActivity;
import org.androcoins.ui.dialog.AppDialogs;
import org.androcoins.ui.dialog.ExpenseMenuDialogFragment;
import org.androcoins.ui.dialog.listener.ExpenseItemClickListener;
import org.androcoins.ui.dialog.listener.ExpenseMenuListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Edit expenses fragment
 * @author vitaly gashock
 */
public class ExpensesEditFragment extends SherlockFragment {
    protected static final int ACTION_BAR_MENU_ID = 1;
    protected static final int ACTION_ADD_ID = 10;

    private ListView mListExpences;

    private ExpensesDbAdapter mExpenseDbAdapter;
    private ExpensesListAdapter mListAdapter;
    private TextView mEmptyView;
    private List<ExpenseHolder> mExpenses = new ArrayList<ExpenseHolder>();

    private ExpensesEditPagerScreen mScreen = ExpensesEditPagerScreen.TODAY;

    private ExpensesEditFragment(ExpensesEditPagerScreen screen) {
        mScreen = screen;
    }

    public static ExpensesEditFragment newInstance(ExpensesEditPagerScreen screen) {
        final ExpensesEditFragment fragment = new ExpensesEditFragment(screen);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mExpenseDbAdapter = new ExpensesDbAdapter(activity);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadExpensesList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_expenses_edit, container, false);

        mEmptyView = (TextView) root.findViewById(R.id.txt_empty);

        mListExpences = (ListView) root.findViewById(R.id.list_expences);
        mListExpences.setEmptyView(mEmptyView);

        registerForContextMenu(mListExpences);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(ACTION_BAR_MENU_ID, ACTION_ADD_ID, 0, R.string.action_add)
                .setIcon(R.drawable.ico_action_add)
                .setShowAsAction(com.actionbarsherlock.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ACTION_ADD_ID :
                requestNewExpenseViewOpening();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final ExpenseEntity expense = (ExpenseEntity) mListAdapter.getItem(info.position);
        showContextDialogFor(expense);
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
        dialog.show(getActivity().getSupportFragmentManager(), "expense-context-dialog");
    }

    private void loadExpensesList() {
        mExpenses = loadExpenses();

        final ExpenseItemClickListener clickListener = new ExpenseItemClickListener() {
            @Override
            public void onItemClicked(ExpenseEntity expense) {
                requestEditExpenseViewOpening(expense);
            }

            @Override
            public void onItemContextClicked(ExpenseEntity expense) {
                showContextDialogFor(expense);
            }
        };

        mListAdapter = new ExpensesListAdapter(getActivity(), mExpenses, clickListener);
        mListExpences.setAdapter(mListAdapter);
    }

    private List<ExpenseHolder> loadExpenses() {
        switch (mScreen) {
            case TODAY :
                return mExpenseDbAdapter.getTodayExpenseHolders();
            case YESTERDAY :
                return mExpenseDbAdapter.getYesterdayExpenseHolders();
            case WEEK :
                return mExpenseDbAdapter.getWeeklyExpenseHolders();
            case MONTH :
                return mExpenseDbAdapter.getMonthlyExpenseHolders();
        }
        return new ArrayList<ExpenseHolder>();  // empty
    }

    private void requestNewExpenseViewOpening() {
        final Intent newExpenseIntent = new Intent(getActivity(), EditorActivity.class);
        startActivityForResult(newExpenseIntent, AppConstants.APP_EDIT_TODAY_RESULT_CODE);
    }

    private void requestEditExpenseViewOpening(ExpenseEntity expense) {
        final Intent editExpenseIntent = new Intent(getActivity(), EditorActivity.class);
        editExpenseIntent.putExtra(AppConstants.EDIT_EXPENSE_ID_KEY, expense.id);
        startActivityForResult(editExpenseIntent, AppConstants.APP_EDIT_TODAY_RESULT_CODE);
    }

    private void tryToRemoveExpenseItem(final ExpenseEntity expense) {
        AppDialogs.confirm(getActivity(), R.string.remove_today_item_title,
                R.string.remove_today_item_confirm,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean result = mExpenseDbAdapter.removeEntity(expense);
                        if (result) {
                            loadExpensesList();
                        }
                    }
                }
        );
    }
}
