package com.riders.thelab.ui.mainactivity.fragment.news;

import com.riders.thelab.ui.base.BaseView;

import javax.inject.Inject;

public class NewsFragmentView implements BaseView {

    NewsFragment newsFragment;

    @Inject
    NewsFragmentView(NewsFragment newsFragment) {
        this.newsFragment = newsFragment;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {

    }
}
