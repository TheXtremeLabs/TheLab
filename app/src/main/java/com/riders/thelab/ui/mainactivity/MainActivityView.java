package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;

import com.riders.thelab.data.local.model.App;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class MainActivityView extends BaseViewImpl<MainActivityPresenter>
        implements MainActivityContract.View {

    // TAG & Context
    private MainActivity context;

    //Views
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

        // Butterknife view binding
        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        // Attach view with presenter
        getPresenter().attachView(this);

        initViews();

        // Call presenter to fetch data
        getPresenter().getPackageList();
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

    }

    @Override
    public void onErrorPackageList() {

    }


    @Override
    public void closeApp() {
        context.finish();
    }
    /////////////////////////////////////
    //
    // PRESENTER
    //
    /////////////////////////////////////
}
