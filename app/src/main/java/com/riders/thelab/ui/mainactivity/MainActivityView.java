package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.interfaces.ConnectivityListener;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.LabNetworkManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.core.views.ItemSnapHelper;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.base.BaseViewImpl;
import com.riders.thelab.ui.mainactivity.fragment.news.NewsFragment;
import com.riders.thelab.ui.mainactivity.fragment.time.TimeFragment;
import com.riders.thelab.ui.mainactivity.fragment.weather.WeatherFragment;
import com.riders.thelab.utils.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.ButterKnife;
import timber.log.Timber;


@SuppressLint({"NonConstantResourceId", "NewApi"})
public class MainActivityView extends BaseViewImpl<MainActivityPresenter>
        implements MainActivityContract.View, MainActivityAppClickListener,
        ConnectivityListener, MenuItem.OnMenuItemClickListener {

    // TAG & Context
    private MainActivity context;

    //Views
    ViewPager2 viewPager2;
    Toolbar toolbar;
    LinearLayout layoutDots;
    RecyclerView appRecyclerView;
    FragmentContainerView timeFragmentContainerView;
    FragmentContainerView weatherFragmentContainerView;
    ConstraintLayout clDetailItem;
    ShapeableImageView ivItemDetail;
    MaterialTextView tvTitleDetail;
    MaterialTextView tvDescriptionDetail;
    MaterialButton goButton;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;
    private ConnectivityManager mConnectivityManager;
    private LabNetworkManager networkManager;
    private Menu menu;
    private List<Fragment> fragmentList;
    private TextView[] dots;
    /*
     * ViewPager page change listener
     */
    ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            addBottomDots(position);
        }
    };

    @Inject
    MainActivityView(MainActivity context) {
        this.context = context;
    }

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    public void onCreate() {

        // Attach view with presenter
        getPresenter().attachView(this);

        if (!LabCompatibilityManager.isTablet(context)) {
            bindSmartphoneViews();
            initViews();
        } else {
            bindTabletViews();
        }

        // Call presenter to fetch data
        getPresenter().getApplications();
    }

    @Override
    public void onStart() {

        mConnectivityManager =
                (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkManager = new LabNetworkManager(this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onPause() {
        Timber.e("onPause()");
        if (LabCompatibilityManager.isLollipop()) {
            mConnectivityManager.unregisterNetworkCallback(networkManager);
        }
    }

    @Override
    public void onResume() {
        Timber.d("onResume()");
//        TheLabApplication.getInstance().setConnectivityListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // register connection status listener
            NetworkRequest request =
                    new NetworkRequest.Builder()
                            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build();

            mConnectivityManager.registerNetworkCallback(request, networkManager);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu) {
        Timber.d("onCreateOptionsMenu()");

        context.getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;

        // Manually checking internet connection
        checkConnection();
    }

    @Override
    public void onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connection_icon:
                UIManager.showActionInToast(context, "Wifi clicked");

                WifiManager wifiManager =
                        (WifiManager) context
                                .getApplicationContext()
                                .getSystemService(Context.WIFI_SERVICE);

                if (!LabCompatibilityManager.isAndroid10()) {
                    boolean isWifi = wifiManager.isWifiEnabled();
                    wifiManager.setWifiEnabled(!isWifi);
                } else {
                    Timber.e("For applications targeting android.os.Build.VERSION_CODES Q or above, this API will always fail and return false");

                    /*
                        ACTION_INTERNET_CONNECTIVITY Shows settings related to internet connectivity, such as Airplane mode, Wi-Fi, and Mobile Data.
                        ACTION_WIFI Shows Wi-Fi settings, but not the other connectivity settings. This is useful for apps that need a Wi-Fi connection to perform large uploads or downloads.
                        ACTION_NFC Shows all settings related to near-field communication (NFC).
                        ACTION_VOLUME Shows volume settings for all audio streams.
                     */
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                    this.startActivityForResult(panelIntent, 955);
                }
                break;

            case R.id.action_settings:
                UIManager.showActionInToast(context, "Settings clicked");
                break;

            case R.id.info_icon:
                showBottomSheetDialogFragment();
                break;

            case R.id.action_force_crash:
                throw new RuntimeException("This is a crash");

            default:
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        Timber.e("startActivityForResult()");
    }

    public void onDestroy() {
        Timber.d("onDestroy()");

        if (LabCompatibilityManager.isLollipop()) {
            Timber.d("unregister network callback()");
            //mConnectivityManager.unregisterNetworkCallback(networkManager);
        }

        getPresenter().detachView();
        context = null;
    }


    /////////////////////////////////////
    //
    // BUTTERKNIFE
    //
    /////////////////////////////////////
    private void bindSmartphoneViews() {
        // Butterknife view binding
        viewPager2 = context.findViewById(R.id.view_pager);
        toolbar = context.findViewById(R.id.toolbar);
        layoutDots = context.findViewById(R.id.layoutDots);
        appRecyclerView = context.findViewById(R.id.app_recyclerView);
    }

    private void bindTabletViews() {
        ButterKnife.bind(context, timeFragmentContainerView);
        ButterKnife.bind(context, weatherFragmentContainerView);
        ButterKnife.bind(context, context.findViewById(R.id.app_recyclerView));

        context.getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.fragment_time,
                        TimeFragment.newInstance())
                .commit();

        context.getSupportFragmentManager()
                .beginTransaction()
                .add(
                        R.id.fragment_weather,
                        WeatherFragment.newInstance())
                .commit();

        clDetailItem = context.findViewById(R.id.cl_detail_item);
        ivItemDetail = context.findViewById(R.id.iv_item_detail);
        tvTitleDetail = context.findViewById(R.id.tv_title_detail);
        tvDescriptionDetail = context.findViewById(R.id.tv_description_detail);
        goButton = context.findViewById(R.id.item_detail_btn);
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////

    // Method to manually check connection status
    private void checkConnection() {
        Timber.d("checkConnection()");

        boolean isConnected = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            isConnected = LabNetworkManager.isConnected(context);
        }
        UIManager.showConnectionStatusInSnackBar(context, isConnected);

        updateToolbarConnectionIcon(isConnected);
    }

    public void showBottomSheetDialogFragment() {
        BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
        bottomSheetFragment.show(
                context.getSupportFragmentManager(),
                bottomSheetFragment.getTag());
    }

    /**
     * Set up views (recyclerviews, spinner, etc...)
     */
    private void initViews() {

        initCollapsingToolbar();
        initToolbar();

        // Instantiate a ViewPager2 and a PagerAdapter.
        fragmentList = new ArrayList<>();

        // add Fragments in your ViewPagerFragmentAdapter class
        fragmentList.add(new TimeFragment());
        fragmentList.add(new WeatherFragment());
        fragmentList.add(new NewsFragment());

        pagerAdapter =
                new ViewPager2Adapter(
                        context.getSupportFragmentManager(),
                        context.getLifecycle(),
                        fragmentList);
        // set Orientation in your ViewPager2
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        viewPager2.setAdapter(pagerAdapter);
        viewPager2.registerOnPageChangeCallback(pageChangeCallback);
    }

    /*
     * Adds bottom dots indicator
     * */
    private void addBottomDots(int currentPage) {
        dots = new TextView[fragmentList.size()];

        int[] colorsActive = context.getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = context.getResources().getIntArray(R.array.array_dot_inactive);

        layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(context);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar txtPostTitle on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                context.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = context.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);
        // hiding & showing the txtPostTitle when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    // Toolbar is collapsed
                    collapsingToolbar.setTitle(context.getResources().getString(R.string.app_name));
                    showMenuButtons();
                    isShow = true;

                } else if (isShow) {
                    // Toolbar is expanded
                    collapsingToolbar.setTitle(" ");
                    hideMenuButtons();
                    isShow = false;
                }
            }
        });

    }

    /**
     * Setup Toolbar menu icon differently than the basic way because of the collapsing toolbar
     * <p>
     * We want the button to show up only when the tollbar is collapsed
     * <p>
     * https://stackoverflow.com/questions/10692755/how-do-i-hide-a-menu-item-in-the-actionbar#:~:text=The%20best%20way%20to%20hide,menu%20inside%20the%20same%20group.&text=Then%2C%20on%20your%20activity%20(preferable,visibility%20to%20false%20or%20true.
     */
    private void initToolbar() {

        toolbar = context.findViewById(R.id.toolbar);

        toolbar.inflateMenu(R.menu.menu_main);
        this.menu = toolbar.getMenu();
        hideMenuButtons();
        toolbar.setOnMenuItemClickListener(this::onMenuItemClick);
    }

    /**
     * Display menu buttons when collapsing toolbar is collapsed
     */
    public void showMenuButtons() {
        if (menu != null)
            menu.setGroupVisible(R.id.menu_main_group, true); // Or true to be visible
    }

    /**
     * Hide menu buttons when collapse toolbar is expanded
     */
    public void hideMenuButtons() {
        if (menu != null)
            menu.setGroupVisible(R.id.menu_main_group, false); // Or true to be visible
    }

    private void showItemDetail(App app) {

        if (View.INVISIBLE == clDetailItem.getVisibility())
            clDetailItem.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(
                        (0 != app.getIcon())
                                ? app.getIcon()
                                : app.getDrawableIcon())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }


                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {

                        if (goButton.getVisibility() == View.GONE) {
                            goButton.setVisibility(View.VISIBLE);
                        }

                        if (0 != app.getIcon() && app.getTitle().equals("Palette")) {
                            Bitmap myBitmap = ((BitmapDrawable) resource).getBitmap();

                            Bitmap newBitmap = UIManager.addGradientToImageView(context, myBitmap);
                            ivItemDetail.setImageDrawable(
                                    new BitmapDrawable(context.getResources(), newBitmap));
                            return true;
                        }

                        if (0 != app.getIcon() && app.getTitle().equals("WIP")) {
                            ivItemDetail.setImageDrawable(
                                    ContextCompat.getDrawable(context, R.drawable.logo_testing));
                            goButton.setVisibility(View.GONE);
                            return true;
                        }

                        return false;
                    }
                })
                .into(ivItemDetail);

        tvTitleDetail.setText(!Validator.isEmpty(app.getTitle())
                ? app.getTitle()
                : app.getName());

        tvDescriptionDetail.setText(
                !Validator.isEmpty(app.getVersion())
                        ? app.getVersion()
                        : app.getDescription());

    }

    /////////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////////
    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
    }

    @Override
    public void onSuccessPackageList(List<App> applications) {
        Timber.d("onSuccessPackageList()");

        MainActivityAdapter adapter = new MainActivityAdapter(context, applications, this);

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(context,
                        !LabCompatibilityManager.isTablet(context)
                                ? LinearLayoutManager.VERTICAL
                                : LinearLayoutManager.HORIZONTAL, false);

        appRecyclerView = context.findViewById(R.id.app_recyclerView);

        appRecyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        divider.setDrawable(
                Objects.requireNonNull(
                        ContextCompat.getDrawable(
                                context,
                                R.drawable.item_separator_view_gradient)));
        if (!LabCompatibilityManager.isTablet(context))
            appRecyclerView.addItemDecoration(divider);
        else {
            ItemSnapHelper helper = new ItemSnapHelper();
            helper.attachToRecyclerView(appRecyclerView);
        }

        appRecyclerView.setItemAnimator(new DefaultItemAnimator());
        appRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onErrorPackageList() {
        Timber.e("onErrorPackageList()");
    }


    @Override
    public void closeApp() {
        context.finish();
    }

    @Override
    public void onAppItemCLickListener(View view, App item, int position) {

        Timber.d("Clicked item : " + item + ", at position : " + position);

        if (!LabCompatibilityManager.isTablet(context)) {
            getPresenter().launchActivityOrPackage(item);
        } else {
            showItemDetail(item);
            goButton.setOnClickListener(v -> getPresenter().launchActivityOrPackage(item));
        }
    }

    @Override
    public void onConnected() {
        UIManager.showConnectionStatusInSnackBar(context, true);

        updateToolbarConnectionIcon(true);
    }

    @Override
    public void onLostConnection() {

        UIManager.showConnectionStatusInSnackBar(context, false);

        updateToolbarConnectionIcon(false);
    }

    private void updateToolbarConnectionIcon(boolean isConnected) {
        Timber.e("updateToolbarConnectionIcon, is connected : %s", isConnected);

        context.runOnUiThread(() -> {
            if (null != menu)
                menu
                        .getItem(0)
                        .setIcon(
                                ContextCompat.getDrawable(
                                        context,
                                        isConnected
                                                ? R.drawable.ic_wifi
                                                : R.drawable.ic_wifi_off));

        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.connection_icon:
                UIManager.showActionInToast(context, "Wifi clicked");

                WifiManager wifiManager =
                        (WifiManager) context
                                .getApplicationContext()
                                .getSystemService(Context.WIFI_SERVICE);

                if (!LabCompatibilityManager.isAndroid10()) {
                    boolean isWifi = wifiManager.isWifiEnabled();
                    wifiManager.setWifiEnabled(!isWifi);
                } else {
                    Timber.e("For applications targeting android.os.Build.VERSION_CODES Q or above, this API will always fail and return false");

                    /*
                        ACTION_INTERNET_CONNECTIVITY Shows settings related to internet connectivity, such as Airplane mode, Wi-Fi, and Mobile Data.
                        ACTION_WIFI Shows Wi-Fi settings, but not the other connectivity settings. This is useful for apps that need a Wi-Fi connection to perform large uploads or downloads.
                        ACTION_NFC Shows all settings related to near-field communication (NFC).
                        ACTION_VOLUME Shows volume settings for all audio streams.
                     */
                    Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
                    this.startActivityForResult(panelIntent, 955);
                }
                break;

            case R.id.action_settings:
                UIManager.showActionInToast(context, "Settings clicked");
                break;

            case R.id.info_icon:
                showBottomSheetDialogFragment();
                break;

            case R.id.action_force_crash:
                throw new RuntimeException("This is a crash");

            default:
                break;
        }
        return true;
    }
    /////////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////////
}
