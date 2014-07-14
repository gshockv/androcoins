package org.androcoins.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.SubMenu;
import org.androcoins.R;
import org.androcoins.data.db.ExpensesDbAdapter;
import org.androcoins.data.model.ExpenseHolder;
import org.androcoins.ui.UIUtils;
import org.androcoins.ui.dialog.SelectDateDialogFragment;
import org.androcoins.ui.dialog.listener.SelectDateListener;
import org.androcoins.util.AppUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * Report activity
 *
 * @author vitaly gashock
 */

public class ReportActivity extends AppBaseActivity {
    private static final int SUBMENU_ITEM_ID = 100600;
    private static final int TODAY_ACTION_ID = 100601;
    private static final int WEEK_ACTION_ID = 100602;
    private static final int MONTH_ACTION_ID = 100603;
    private static final int DATE_ACTION_ID = 100604;
    private static final int PERIOD_ACTION_ID = 100605;

    private View mReportView;
    private LinearLayout mExpensesContainer;
    private TextView mEmptyView;

    private ExpensesDbAdapter mDbAdapter;

    private enum ReportType {
        TODAY,
        WEEK,
        MONTH,
        BY_DATE,
        BY_RANGE
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mReportView = findViewById(R.id.view_report);
        mEmptyView = (TextView) findViewById(R.id.txt_empty);
        mDbAdapter = new ExpensesDbAdapter(getApplicationContext());
        mExpensesContainer = (LinearLayout) findViewById(R.id.container);

        buildReport(ReportType.TODAY); // by default
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final SubMenu subMenu = menu.addSubMenu("");
        subMenu.add(SUBMENU_ITEM_ID, TODAY_ACTION_ID, 0, R.string.today_report);
        subMenu.add(SUBMENU_ITEM_ID, WEEK_ACTION_ID, 1, R.string.week_report);
        subMenu.add(SUBMENU_ITEM_ID, MONTH_ACTION_ID, 2, R.string.month_report);
        subMenu.add(SUBMENU_ITEM_ID, DATE_ACTION_ID, 3, R.string.date_report);
        subMenu.add(SUBMENU_ITEM_ID, PERIOD_ACTION_ID, 4, R.string.period_report);
        subMenu.getItem()
                .setIcon(R.drawable.ico_action_select)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case TODAY_ACTION_ID:
                buildReport(ReportType.TODAY);
                return true;
            case WEEK_ACTION_ID:
                buildReport(ReportType.WEEK);
                return true;
            case MONTH_ACTION_ID:
                buildReport(ReportType.MONTH);
                return true;
            case DATE_ACTION_ID:
                startByDateReportBuilding();
                return true;
            case PERIOD_ACTION_ID:
                startByRangeReportBuilding();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Date startDate;
    private Date endDate;

    private void startByDateReportBuilding() {
        final SelectDateListener listener = new SelectDateListener() {
            @Override
            public void dateSelected(Date date) {
                startDate = date;
                buildReport(ReportType.BY_DATE);
            }
        };
        final SelectDateDialogFragment dlg = new SelectDateDialogFragment(R.string.dialog_select_date_title, listener);
        dlg.show(getSupportFragmentManager(), "select-date-dialog");
    }

    private void startByRangeReportBuilding() {
        // fucking chaining
        final SelectDateListener endDateListener = new SelectDateListener() {
            @Override
            public void dateSelected(Date date) {
                endDate = date;
                buildReport(ReportType.BY_RANGE);
            }
        };

        final SelectDateListener startDateListener = new SelectDateListener() {
            @Override
            public void dateSelected(Date date) {
                startDate = date;
                final SelectDateDialogFragment endDateDialog =
                        new SelectDateDialogFragment(R.string.dialog_select_end_date_title, endDateListener);
                endDateDialog.show(getSupportFragmentManager(), "end-date-dialog");
            }
        };

        final SelectDateDialogFragment startDateDialog =
                new SelectDateDialogFragment(R.string.dialog_select_start_date_title, startDateListener);
        startDateDialog.show(getSupportFragmentManager(), "start-date-dialog");
    }

    private void buildReport(ReportType type) {
        getSupportActionBar().setTitle(getTitleFor(type));

        mExpensesContainer.removeAllViews();

        final List<ExpenseHolder> expenseHolders = getExpensesFor(type);
        if (expenseHolders.isEmpty()
                || (expenseHolders.size() == 1 && expenseHolders.get(0).expenses.isEmpty())) {
            // display empty view
            mReportView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mReportView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);

            // display report
            final TextView totalView = (TextView) findViewById(R.id.txt_report_total);
            totalView.setText(getTotalAmountsFor(type));

            for (ExpenseHolder holder : expenseHolders) {
                final ViewGroup card = UIUtils.Widget.createCardReportView(this, holder);
                mExpensesContainer.addView(card);
            }
        }
    }

    private List<ExpenseHolder> getExpensesFor(ReportType type) {
        switch (type) {
            case TODAY:
                return mDbAdapter.getTodayExpenseHolders();
            case WEEK:
                return mDbAdapter.getWeeklyExpenseHolders();
            case MONTH:
                return mDbAdapter.getMonthlyExpenseHolders();
            case BY_DATE :
                return mDbAdapter.getByDateExpenseHolders(startDate);
            case BY_RANGE :
                return mDbAdapter.getByRangeExpenseHolders(startDate, endDate);
            default:
                throw new UnsupportedOperationException("Unsupported report type");
        }
    }

    private String getTotalAmountsFor(ReportType type) {
        switch (type) {
            case TODAY:
                return AppUtils.formatToCurrency(mDbAdapter.getTodayRegisteredAmounts());
            case WEEK:
                return AppUtils.formatToCurrency(mDbAdapter.getWeekRegisteredAmounts());
            case MONTH:
                return AppUtils.formatToCurrency(mDbAdapter.getMonthRegisteredAmounts());
            case BY_DATE:
                return AppUtils.formatToCurrency(mDbAdapter.getByDateRegisteredAmounts(startDate));
            case BY_RANGE:
                return AppUtils.formatToCurrency(mDbAdapter.getByRangeRegisteredAmounts(startDate, endDate));
            default:
                return "";
        }
    }

    private String getTitleFor(ReportType type) {
        switch (type) {
            case TODAY:
                return getString(R.string.today_report_title);
            case WEEK:
                return getString(R.string.week_report_title);
            case MONTH:
                return getString(R.string.month_report_title);
            case BY_DATE:
                return getString(R.string.date_report_title,
                        DateFormat.getDateInstance(DateFormat.SHORT).format(startDate));
            case BY_RANGE:
                return getString(R.string.period_report_title,
                        DateFormat.getDateInstance(DateFormat.SHORT).format(startDate),
                        DateFormat.getDateInstance(DateFormat.SHORT).format(endDate));
            default:
                throw new UnsupportedOperationException("Unsupported report type");
        }
    }
}
