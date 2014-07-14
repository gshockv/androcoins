package org.androcoins.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import org.androcoins.data.ExpenseTable;
import org.androcoins.data.model.CategoryDetailsEntity;
import org.androcoins.data.model.ExpenseEntity;
import org.androcoins.data.model.ExpenseHolder;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * Holds all expense-related operations with internal database
 * @author vitaly gashock
 */
public class ExpensesDbAdapter extends DbAdapter<ExpenseEntity> {
	private Context mContext;
	
	public ExpensesDbAdapter(Context ctx) {
		super(ctx);
		this.mContext = ctx;
	}
	
	@Override
	public ExpenseEntity loadEntity(long id) {
		final SQLiteDatabase db = mDbHelper.getReadableDatabase();
		final Cursor cursor = db.query(ExpenseTable.NAME, ExpenseTable.getColumns(), 
				DbAdapter.KEY_COLUNM + " = " + id, 
				null, null, null, null);
		
		ExpenseEntity expense = null;
		if (cursor.moveToFirst()) {
			expense = extractExpense(cursor);
		}
		db.close();
		return expense;
	}
	
	public double getTodayRegisteredAmounts() {
		String sql = "SELECT SUM(amount) FROM " + ExpenseTable.NAME
			+ " WHERE " + ExpenseTable.DATE_COLUMN 
			+ " = \'" 
			+ DateFormat.getDateInstance(DateFormat.SHORT).format(new Date())
			+ "\'";

        return executeSumQuery(sql);
	}

    public double getWeekRegisteredAmounts() {
        final Date today = new Date();
        final Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(today);
        int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int offset = cal.getFirstDayOfWeek() == 0 ? 1 : 2;
        cal.add(Calendar.DAY_OF_YEAR, (currentDayOfWeek * -1) + offset);

        String sql = "SELECT SUM(amount) FROM " + ExpenseTable.NAME
                + " WHERE " + ExpenseTable.DATE_COLUMN
                + " BETWEEN \'"
                + DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime())
                + "\' AND \'"
                + DateFormat.getDateInstance(DateFormat.SHORT).format(today)
                + "\'";

        return executeSumQuery(sql);
    }

    public double getMonthRegisteredAmounts() {
        final Date today = new Date();
        final Date firstMonthDay = new Date();
        firstMonthDay.setDate(1);

        String sql = "SELECT SUM(amount) FROM " + ExpenseTable.NAME
                + " WHERE " + ExpenseTable.DATE_COLUMN
                + " BETWEEN \'"
                + DateFormat.getDateInstance(DateFormat.SHORT).format(firstMonthDay)
                + "\' AND \'"
                + DateFormat.getDateInstance(DateFormat.SHORT).format(today)
                + "\'";

        return executeSumQuery(sql);
    }

    public double getByDateRegisteredAmounts(Date date) {
        String sql = "SELECT SUM(amount) FROM " + ExpenseTable.NAME
                + " WHERE " + ExpenseTable.DATE_COLUMN
                + " = \'"
                + DateFormat.getDateInstance(DateFormat.SHORT).format(date)
                + "\'";
        return executeSumQuery(sql);
    }

    public double getByRangeRegisteredAmounts(Date startDate, Date endDate) {
        String sql = "SELECT SUM(amount) FROM " + ExpenseTable.NAME
                + " WHERE " + ExpenseTable.DATE_COLUMN
                + " BETWEEN \'"
                + DateFormat.getDateInstance(DateFormat.SHORT).format(startDate)
                + "\' AND \'"
                + DateFormat.getDateInstance(DateFormat.SHORT).format(endDate)
                + "\'";

        return executeSumQuery(sql);
    }

    public List<ExpenseHolder> getTodayExpenseHolders() {
        final List<ExpenseHolder> holders = new ArrayList<ExpenseHolder>();
        holders.add(new ExpenseHolder(new Date(), getExpensesForDate(new Date())));
        return holders;
    }

    public List<ExpenseHolder> getYesterdayExpenseHolders() {
        final Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);

        final List<ExpenseHolder> holders = new ArrayList<ExpenseHolder>();
        holders.add(new ExpenseHolder(yesterday.getTime(), getExpensesForDate(yesterday.getTime())));
        return holders;
    }

    public List<ExpenseHolder> getWeeklyExpenseHolders() {
        final Date today = new Date();
        final Calendar weekFirstDay = Calendar.getInstance(Locale.getDefault());
        weekFirstDay.setTime(today);

        final List<ExpenseHolder> result = new ArrayList<ExpenseHolder>();
        Calendar day = Calendar.getInstance(Locale.getDefault());
        day.setTime(today);

        int bound = weekFirstDay.getFirstDayOfWeek() - 1;

        for (int d = day.get(Calendar.DAY_OF_WEEK); d >= bound; d--) {
            final List<ExpenseEntity> expenses = getExpensesForDate(day.getTime());
            if (!expenses.isEmpty()) {
                result.add(new ExpenseHolder(day.getTime(), expenses));
            }
            day.add(Calendar.DATE, -1);
        }
        return result;
    }

    public List<ExpenseHolder> getMonthlyExpenseHolders() {
        Calendar day = Calendar.getInstance(Locale.getDefault());
        day.setTime(new Date());

        final List<ExpenseHolder> result = new ArrayList<ExpenseHolder>();
        for (int d = day.get(Calendar.DAY_OF_MONTH); d >= 1; d--) {
            final List<ExpenseEntity> expenses = getExpensesForDate(day.getTime());
            if (!expenses.isEmpty()) {
                result.add(new ExpenseHolder(day.getTime(), expenses));
            }
            day.add(Calendar.DATE, -1);
        }
        return result;
    }

    public List<ExpenseHolder> getByDateExpenseHolders(Date date) {
        final List<ExpenseEntity> expenses = getExpensesForDate(date);
        final List<ExpenseHolder> result = new ArrayList<ExpenseHolder>();
        result.add(new ExpenseHolder(date, expenses));
        return result;
    }

    public List<ExpenseHolder> getByRangeExpenseHolders(Date startDate, Date endDate) {
        final List<ExpenseHolder> result = new ArrayList<ExpenseHolder>();

        if (startDate.after(endDate)) {
            return result;
        }

        final Calendar currentDay = Calendar.getInstance(Locale.getDefault());
        currentDay.setTime(startDate);
        while (currentDay.getTime().compareTo(endDate) <= 0) {
            final List<ExpenseEntity> expenses = getExpensesForDate(currentDay.getTime());
            if (!expenses.isEmpty()) {
                result.add(new ExpenseHolder(currentDay.getTime(), expenses));
            }
            currentDay.add(Calendar.DATE, 1);
        }

        return result;
    }

    private List<ExpenseEntity> getExpensesForDate(Date date) {
        final SQLiteDatabase db = mDbHelper.getReadableDatabase();
        final Cursor cursor = db.query(ExpenseTable.NAME, ExpenseTable.getColumns(),
                ExpenseTable.DATE_COLUMN + " = \'" + DateFormat.getDateInstance(DateFormat.SHORT).format(date)
                        + "\'",
                null, null, null, null);

        final List<ExpenseEntity> expenses = extractExpensesList(cursor);
        db.close();
        return expenses;
    }

	public CategoryDetailsEntity getAllExpensesForCategory(long categoryId) {
		final CategoryDetailsEntity details = new CategoryDetailsEntity();
		// today registered expenses
		details.todayExpenses = getTodaySummarizedExpensesForCategory(categoryId);
		// registered expenses for the current week
		details.weekExpenses = getWeeklySummarizedExpensesForCategory(categoryId);
		// registered expenses for the current month
		details.monthExpenses = getMonthlySummarizedExpensesForCategory(categoryId);
		// all registered expenses
		details.totalExpenses = getAllSummarizedExpensesForCategory(categoryId);
		
		return details;
	}
	
	private double getTodaySummarizedExpensesForCategory(long categoryId) {
		String sql = "SELECT SUM(amount) FROM " + ExpenseTable.NAME
			+ " WHERE " + ExpenseTable.DATE_COLUMN 
			+ " = \'" 
			+ DateFormat.getDateInstance(DateFormat.SHORT).format(new Date())
			+ "\' AND " 
			+ ExpenseTable.CATEGORY_COLUMN
			+ " = "
			+ categoryId;
		
		return executeSumQuery(sql);
	}
	
	private double getWeeklySummarizedExpensesForCategory(long categoryId) {
		final Date today = new Date();
		final Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTime(today);
		int currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int offset = cal.getFirstDayOfWeek() == 0 ? 1 : 2;
		cal.add(Calendar.DAY_OF_YEAR, (currentDayOfWeek * -1) + offset);
		
		String sql = "SELECT SUM(amount) FROM " + ExpenseTable.NAME
			+ " WHERE " + ExpenseTable.DATE_COLUMN 
			+ " BETWEEN \'" 
			+ DateFormat.getDateInstance(DateFormat.SHORT).format(cal.getTime())
			+ "\' AND \'" 
			+ DateFormat.getDateInstance(DateFormat.SHORT).format(today)
			+ "\' AND "
			+ ExpenseTable.CATEGORY_COLUMN
			+ " = "	+ categoryId;
		
		return executeSumQuery(sql);
	}
	
	private double getMonthlySummarizedExpensesForCategory(long categoryId) {
		final Date today = new Date();
		final Date firstMonthDay = new Date();
		firstMonthDay.setDate(1);
		
		String sql = "SELECT SUM(amount) FROM " + ExpenseTable.NAME
			+ " WHERE " + ExpenseTable.DATE_COLUMN 
			+ " BETWEEN \'" 
			+ DateFormat.getDateInstance(DateFormat.SHORT).format(firstMonthDay)
			+ "\' AND \'" 
			+ DateFormat.getDateInstance(DateFormat.SHORT).format(today)
			+ "\' AND "
			+ ExpenseTable.CATEGORY_COLUMN
			+ " = "	+ categoryId;
	
		return executeSumQuery(sql);
	}
	
	private double getAllSummarizedExpensesForCategory(long categoryId) {
		String sql = "SELECT SUM(amount) FROM " + ExpenseTable.NAME
			+ " WHERE " + ExpenseTable.CATEGORY_COLUMN
			+ " = "
			+ categoryId;
		
		return executeSumQuery(sql);
	}
	
	private double executeSumQuery(String sql) {
		double result = 0.0;
		final SQLiteDatabase db = mDbHelper.getReadableDatabase();
		try {
			Cursor rawResults = db.rawQuery(sql, null);
			
			if (rawResults.moveToFirst()) {
				result = rawResults.getDouble(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
		return result;
	}
	
	public boolean removeExpensesForCategory(long categoryId) {
		final SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int result = db.delete(ExpenseTable.NAME, ExpenseTable.CATEGORY_COLUMN + " = " + categoryId, null);
		db.close();
		return result > 0;
	}
	
	private List<ExpenseEntity> extractExpensesList(final Cursor cursor) {
		final List<ExpenseEntity> expenses = new LinkedList<ExpenseEntity>();
		
		if (cursor.moveToFirst()) {
			do {
				expenses.add(extractExpense(cursor));
			} while (cursor.moveToNext());
		}
		return expenses;
	}
	
	private ExpenseEntity extractExpense(final Cursor cursor) {
		int id = cursor.getInt(0);
		double amount = cursor.getDouble(1);
		long categoryId = cursor.getLong(2);
		String summary = cursor.getString(3);

        final ExpenseEntity expense = new ExpenseEntity(id, amount, summary);

        try {
            Date registrationDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(cursor.getString(4));
            expense.date = registrationDate;
        } catch (ParseException e) {
            Log.e("ExpensesDbAdapter", "Expense date extraction error: " + e.getMessage());
        }

		final CategoriesDbAdapter categoriesDbAdapter = new CategoriesDbAdapter(mContext);
        expense.category = categoriesDbAdapter.loadEntity(categoryId);
		
		return expense;
	}
}
