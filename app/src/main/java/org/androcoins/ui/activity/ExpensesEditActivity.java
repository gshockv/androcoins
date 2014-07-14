package org.androcoins.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import org.androcoins.R;
import org.androcoins.ui.ExpensesEditPagerScreen;
import org.androcoins.ui.fragment.ExpensesEditFragment;

/**
 * Edit registered expenses activity
 * @author vitaly gashock
 */

public class ExpensesEditActivity extends AppBaseActivity {

    private ViewPager mViewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_expenses_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ExpensesPagerAdapter pageAdapter = new ExpensesPagerAdapter();
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(pageAdapter);
        final PagerTabStrip strip = (PagerTabStrip) findViewById(R.id.pager_tab_strip);
        strip.setTabIndicatorColorResource(R.color.HoloLightBlue);
	}

    private class ExpensesPagerAdapter extends FragmentStatePagerAdapter {
        private ExpensesPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            return ExpensesEditFragment.newInstance(ExpensesEditPagerScreen.values()[position]);
        }

        @Override
        public int getCount() {
            return ExpensesEditPagerScreen.values().length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (ExpensesEditPagerScreen.values()[position]) {
                case TODAY:
                    return getString(R.string.edit_today).toUpperCase();
                case YESTERDAY:
                    return getString(R.string.edit_yesterday).toUpperCase();
                case WEEK:
                    return getString(R.string.edit_week).toUpperCase();
                case MONTH:
                    return getString(R.string.edit_month).toUpperCase();
            }
            return "";
        }
    }
}
