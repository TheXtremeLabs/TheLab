package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


@SuppressLint("NonConstantResourceId")
public class MainActivityView extends BaseViewImpl<MainActivityPresenter>
        implements MainActivityContract.View, MainActivityAppClickListener {

    // TAG & Context
    private MainActivity context;

    //Views
    @BindView(R.id.app_recyclerView)
    RecyclerView appRecyclerView;
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
    public void onActivityCreated() {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDetach() {

    }

    public void onDestroy() {
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
                        (ViewGroup) context.findViewById(R.id.content_loader));
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

        // Just use these following two lines,
        // so you can launch any installed application whose package name is known:
        getPresenter().launchIntentForPackage(item.getPackageName());
    }
    /////////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////////
}
