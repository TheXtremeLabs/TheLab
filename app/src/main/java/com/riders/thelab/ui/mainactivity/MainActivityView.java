package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.core.interfaces.ConnectivityListener;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.LabNetworkManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


@SuppressLint({"NonConstantResourceId", "NewApi"})
public class MainActivityView extends BaseViewImpl<MainActivityPresenter>
        implements MainActivityContract.View, MainActivityAppClickListener,
        ConnectivityListener {

    // TAG & Context
    private MainActivity context;

    //Views
    @BindView(R.id.app_recyclerView)
    RecyclerView appRecyclerView;

    private ConnectivityManager mConnectivityManager;
    private LabNetworkManager networkManager;

    private Menu menu;

    /*@BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar_api)
    ProgressBar progressBar;
    @BindView(R.id.linear_fetching_data)
    LinearLayout linearFetchData;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;
    @BindView(R.id.btn_quit_app)
    Button btnExitApp;*/

    //@Inject
    //MoviesApiListAdapter apiAdapter;

    //@Inject
    //MovieDatabaseListAdapter databaseAdapter;


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

        // Butterknife view binding
        ButterKnife.bind(this, context.findViewById(android.R.id.content));

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
        this.menu = menu;

        // Manually checking internet connection
        checkConnection();
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
    /*@OnClick(R.id.btn_quit_app)
    void onClickExitApp() {
        closeApp();
    }*/


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

    /**
     * Set up views (recyclerviews, spinner, etc...)
     */
    private void initViews() {

        /*rvMovie.setLayoutManager(new LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        ));
        rvMovie.setAdapter(apiAdapter);*/
    }


    /////////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////////
    @Override
    public void showLoading() {

        //progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {

        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessPackageList(List<App> applications) {
        Timber.d("onSuccessPackageList()");

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View childLayout =
                inflater.inflate(
                        R.layout.content_no_app_found,
                        context.findViewById(R.id.content_loader));
        //parentLayout.addView(childLayout);

        MainActivityAdapter adapter = new MainActivityAdapter(context, applications, this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        appRecyclerView.setLayoutManager(linearLayoutManager);
        appRecyclerView.setItemAnimator(new DefaultItemAnimator());
        appRecyclerView.setAdapter(adapter);

        /*GridLayoutManager gridLayoutManager
                = new GridLayoutManager(context, 2);
        appRecyclerView.setLayoutManager(gridLayoutManager);
        appRecyclerView.setItemAnimator(new DefaultItemAnimator());
        appRecyclerView.setAdapter(adapter);*/
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

        if (null != item.getPackageName()) {
            Timber.d("launchIntentForPackage(%s)", item.getPackageName());

            // Just use these following two lines,
            // so you can launch any installed application whose package name is known:
            getPresenter().launchIntentForPackage(item.getPackageName());
        } else {

            // Prevent app from crashing if you click on WIP item
            if (null != item.getActivity()) {
                Timber.d("launchActivity(%s)", item.getActivity().getSimpleName());

                getPresenter().launchActivity(item.getActivity());
            } else {
                // Just Log wip item
                Timber.e("Cannot launch this activity : %s", item.toString());
            }
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
    /////////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////////
}
