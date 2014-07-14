package org.androcoins.ui.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingActivity;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import org.androcoins.R;

/**
 * Base activity
 * @author vitaly gashock
 */

public abstract class AppBaseActivity extends SlidingFragmentActivity {
    protected static final int ACTION_BAR_MENU_ID = 1;
    protected static final int ACTION_BAR_SUBMENU_ID = 2;

    protected static final int ACTION_ADD_ID = 10;

    private SlidingMenu mSlidingMenu;
    protected ActionBar mActionBar;

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(R.layout.application_layout);
        setBehindContentView(R.layout.app_menu);
        final FrameLayout container = (FrameLayout) findViewById(R.id.app_container);
        container.removeAllViews();
        container.addView(LayoutInflater.from(this).inflate(layoutResId, null));

        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setBehindOffsetRes(R.dimen.AppMenuBehindOffset);
        mSlidingMenu.setShadowDrawable(R.drawable.app_menu_shadow);
        mSlidingMenu.setShadowWidthRes(R.dimen.AppMenuShadowWidth);

        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        showContent();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    /**************************************************************
     * AppMenu items handlers
     *************************************************************/
    public void onTodayClick(View v) {
        if (this instanceof TodayActivity) {
            showContent();
        } else {
            final Intent i = new Intent(this, TodayActivity.class);
            startActivity(i);
        }
    }

    public void onEditClick(View v) {
        if (this instanceof ExpensesEditActivity) {
            showContent();
        } else {
            final Intent i = new Intent(this, ExpensesEditActivity.class);
            startActivity(i);
        }
    }

    public void onReportsClick(View v) {
        if (this instanceof ReportActivity) {
            showContent();
        } else {
            final Intent i = new Intent(this, ReportActivity.class);
            startActivity(i);
        }
    }

    public void onCategoriesClick(View v) {
        if (this instanceof CategoriesActivity) {
            showContent();
        } else {
            final Intent i = new Intent(this, CategoriesActivity.class);
            startActivity(i);
        }
    }

    public void onSettingsClick(View v) {
        final Intent prefsIntent = new Intent(this, AppPreferencesActivity.class);
        startActivity(prefsIntent);
    }
}
