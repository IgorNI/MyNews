package com.materialdesign.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.SimpleArrayMap;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.materialdesign.myapplication.R;
import com.materialdesign.myapplication.api.ApiManager;
import com.materialdesign.myapplication.bean.city.CityInfo;
import com.materialdesign.myapplication.fragment.HistoryFragment;
import com.materialdesign.myapplication.fragment.WangyiNewsFragment;
import com.materialdesign.myapplication.fragment.ZhihuFragment;
import com.materialdesign.myapplication.utils.KeyUtils;
import com.materialdesign.myapplication.utils.NetWorkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
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
    private Context context;

    // Google Map 定位所需
    private static final String TAG = "Google Map";
    private String QUERYADDRESS = "http://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&sensor=true&language=zh_cn";
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    private boolean mLocationPermissionGranted;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private double latitute;
    private double longtitute;
    private String locationInfo;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
                frameLayoutContainer.setPadding(frameLayoutContainer.getPaddingLeft(),
                        insets.getSystemWindowInsetTop() + getActionBarSize
                                (MainActivity.this),
                        frameLayoutContainer.getPaddingRight() + insets.getSystemWindowInsetRight(), // landscape
                        frameLayoutContainer.getPaddingBottom() + insets.getSystemWindowInsetBottom());
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
        requestLocationPermission();
        getDeviceLocation();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putDouble(KeyUtils.KEY_LATITUDE_STR,latitute);
//                bundle.putDouble(KeyUtils.KEY_LONGITUDE_STR,longtitute);
//                bundle.putString(KeyUtils.KEY_LOCATION_INFO_STR,locationInfo);
                intent.setClass(MainActivity.this,SimpleFragmentModeActivity.class);
//                intent.putExtra(KeyUtils.KEY_BUNDLE_STR,bundle);
                startActivityForResult(intent,KeyUtils.REQUEST_CODE);
            }
        });
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
            if (NetWorkUtils.isNetworkConnected(context)) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                        .commit();
                toolbar.setTitle(title);
                mCurrentFragment = fragment;
            }else {
                Toast.makeText(context,getString(R.string.network_failed),Toast.LENGTH_SHORT).show();
            }

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
            case R.id.nav_history:
                fragment = new HistoryFragment();
                break;
        }
        return fragment;
    }

    private void addFragmentAndTitle() {
        simpleArrayMap.put(R.id.nav_zhihu,getString(R.string.zhihu));
        simpleArrayMap.put(R.id.nav_wangyi,getString(R.string.wangyinews));
        simpleArrayMap.put(R.id.nav_history,getString(R.string.history));
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

    // google Map回调
    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    public interface loadMore {
        void loadStart();
        void loadFinish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    // 获取定位的权限
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        mLocationPermissionGranted = true;
    }

    // 获取设备的位置
    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            latitute = mLastKnownLocation.getLatitude();
                            longtitute = mLastKnownLocation.getLongitude();
                            // 获取当前设备的经纬度
                            Subscription subscription = ApiManager.getInstance().getLocationService().
                                    getCityInfo(String.format("%.2f",mLastKnownLocation.getLatitude()) +"," + String.format("%.2f",mLastKnownLocation.getLongitude()),
                                            "zh-CN", "true",getString(R.string.google_maps_key))
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<CityInfo>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            Toast.makeText(getApplicationContext(),getResources().getString(R.string.get_location_failed), Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onNext(CityInfo cityInfo) {
                                            locationInfo = cityInfo.getResults().get(0).getFormatted_address();
                                            String dialogStr = String.format(getString(R.string.your_location_is),locationInfo);
                                            Log.i(TAG, "onNext: " + dialogStr);
                                            AlertDialog dialog = new AlertDialog.Builder(context).setTitle(getString(R.string.location_info))
                                                    .setMessage(dialogStr)
                                                    .setCancelable(true)
                                                    .create();
                                            dialog.show();

                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(),"定位获取失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
