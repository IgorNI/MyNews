package com.materialdesign.myapplication.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.ViewUtils;
import android.util.TypedValue;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;

import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.fragment.WangyiNewsFragment;
import com.materialdesign.myapplication.fragment.ZhihuFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_container)
    FrameLayout frameLayoutContainer;

    private Fragment mCurrentFragment;
    MenuItem currentMenuItem;
    int navigationId;
    SimpleArrayMap<Integer,String> simpleArrayMap = new SimpleArrayMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        addFragmentAndTitle();
        if (savedInstanceState == null) {
            navigationId = getSharedPreferenceNavItem(MainActivity.this);
            if (navigationId == -1) {
                currentMenuItem = navigationView.getMenu().findItem(navigationId);
            }
            if (currentMenuItem == null) {
                currentMenuItem = navigationView.getMenu().findItem(R.id.nav_zhihu);
            }
            if (currentMenuItem != null) {
                Fragment fragment = getFragmentById(currentMenuItem.getItemId());
                String title = simpleArrayMap.get(currentMenuItem.getItemId());
                if (fragment != null) {
                    switchFragment(fragment, title);
                }
            }
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        drawer.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                ViewGroup.MarginLayoutParams lpToolbar = (ViewGroup.MarginLayoutParams) toolbar
                        .getLayoutParams();
                lpToolbar.topMargin += insets.getSystemWindowInsetTop();
                lpToolbar.rightMargin += insets.getSystemWindowInsetRight();
                toolbar.setLayoutParams(lpToolbar);
                // inset the grid top by statusbar+toolbar & the bottom by the navbar (don't clip)
                frameLayoutContainer.setPadding(frameLayoutContainer.getPaddingLeft(),
                        insets.getSystemWindowInsetTop() + getActionBarSize
                                (MainActivity.this),
                        frameLayoutContainer.getPaddingRight() + insets.getSystemWindowInsetRight(), // landscape
                        frameLayoutContainer.getPaddingBottom() + insets.getSystemWindowInsetBottom());

                // we place a background behind the status bar to combine with it's semi-transparent
                // color to get the desired appearance.  Set it's height to the status bar height
                View statusBarBackground = findViewById(R.id.status_bar_background);
                FrameLayout.LayoutParams lpStatus = (FrameLayout.LayoutParams)
                        statusBarBackground.getLayoutParams();
                lpStatus.height = insets.getSystemWindowInsetTop();
                statusBarBackground.setLayoutParams(lpStatus);

                // inset the filters list for the status bar / navbar
                // need to set the padding end for landscape case

                // clear this listener so insets aren't re-applied
                drawer.setOnApplyWindowInsetsListener(null);
                return insets.consumeSystemWindowInsets();
            }
        });
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

    }

    private static int actionBarSize = -1;

    public static int getActionBarSize(Context context) {
        if (actionBarSize < 0) {
            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.actionBarSize, value, true);
            actionBarSize = TypedValue.complexToDimensionPixelSize(value.data, context
                    .getResources().getDisplayMetrics());
        }
        return actionBarSize;
    }

    private void switchFragment(Fragment fragment, String title) {
        if (mCurrentFragment == null || !mCurrentFragment
                .getClass().getName().equals(fragment.getClass().getName()))
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit();
//        toolbar.setTitle(title);
        mCurrentFragment = fragment;
    }

    private Fragment getFragmentById(int itemId) {
        Fragment fragment = null;
        switch (itemId) {
            case R.id.nav_zhihu:
                fragment = new ZhihuFragment();
                break;
            case R.id.nav_wangyi:
                fragment = new WangyiNewsFragment();
                break;
        }
        return fragment;
    }

    private void addFragmentAndTitle() {
        simpleArrayMap.put(R.id.nav_zhihu,getString(R.string.zhihu));
        simpleArrayMap.put(R.id.nav_wangyi,getString(R.string.wangyinews));
    }

    public int getSharedPreferenceNavItem(Context context) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        return shared.getInt(context.getString(R.string.nevigation_item),-1);
    }

    public void putSharedPreferenceNavItem(Context context, int t) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(context.getString(R.string.nevigation_item),t);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        if (currentMenuItem != item && currentMenuItem != null) {
            currentMenuItem.setChecked(false);
            int id = item.getItemId();
            putSharedPreferenceNavItem(MainActivity.this, id);
            currentMenuItem = item;
            currentMenuItem.setChecked(true);
            switchFragment(getFragmentById(currentMenuItem.getItemId()),simpleArrayMap.get(currentMenuItem.getItemId()));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public interface loadMore {
        void loadStart();
        void loadFinish();
    }
}
