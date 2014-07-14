package org.androcoins.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.androcoins.AppConstants;
import org.androcoins.R;
import org.androcoins.data.model.ExpenseEntity;
import org.androcoins.data.model.ExpenseHolder;
import org.androcoins.util.AppUtils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * UI Utils
 * @author vitaly gashock
 */
public final class UIUtils {

    public static class Widget {
        public static ViewGroup createCardReportView(Context ctx, ExpenseHolder expenseHolder) {
            final LayoutInflater inflater = LayoutInflater.from(ctx);
            final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.card_report, null, false);

            final TextView textDate = (TextView) root.findViewById(R.id.txt_title);
            textDate.setText(getFormattedExpenseDate(expenseHolder.date));

            final TextView textTotal = (TextView) root.findViewById(R.id.txt_total);
            textTotal.setText(getTotalAmount(expenseHolder));

            final LinearLayout expensesContainer = (LinearLayout) root.findViewById(R.id.list_expenses);
            fillExpensesList(ctx, inflater, expensesContainer, expenseHolder);

            return root;
        }

        private static void fillExpensesList(Context ctx, LayoutInflater inflater,
                                             LinearLayout container, ExpenseHolder expensesHolder) {
            container.removeAllViews();

            final Iterator<ExpenseEntity> iter = expensesHolder.expenses.iterator();
            while (iter.hasNext()) {
                final ExpenseEntity expense = iter.next();
                final View itemView = inflater.inflate(R.layout.list_item_reported_expense, null);

                Resources res = ctx.getResources();
                int iconResourceId = res.getIdentifier(expense.category.iconName, "drawable", AppConstants.APP_PACKAGE);
                final ImageView imageView = (ImageView) itemView.findViewById(R.id.img_icon);
                imageView.setImageResource(iconResourceId);

                final TextView titleView = (TextView) itemView.findViewById(R.id.txt_title);
                titleView.setText(AppUtils.formatToCurrency(expense.amount));

                final TextView subtitleView = (TextView) itemView.findViewById(R.id.txt_subtitle);
                String summaryText = expense.summary == null ? "" : " - " + expense.summary;
                subtitleView.setText(ctx.getString(R.string.category_with_summary,
                        expense.category.title, summaryText));

                container.addView(itemView);

                if (iter.hasNext()) {
                    final View separator = inflater.inflate(R.layout.separator_view, container, false);
                    container.addView(separator);
                }
                container.forceLayout();
            }
        }

        private static String getTotalAmount(ExpenseHolder expenseHolder) {
            double total = 0.0;
            for (ExpenseEntity expense : expenseHolder.expenses) {
                total += expense.amount;
            }
            return AppUtils.formatToCurrency(total);
        }

        private static String getFormattedExpenseDate(Date date) {
            return DateFormat.getDateInstance(DateFormat.LONG).format(date);
        }
    }

    public static boolean isRobotoRequired() {
        int currentPlatformVersion = Build.VERSION.SDK_INT;
        return currentPlatformVersion <= Build.VERSION_CODES.GINGERBREAD;
    }
}
