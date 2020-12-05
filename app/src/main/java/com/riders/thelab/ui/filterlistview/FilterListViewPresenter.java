package com.riders.thelab.ui.filterlistview;

import com.riders.thelab.navigator.Navigator;
import com.riders.thelab.ui.base.BasePresenterImpl;

import javax.inject.Inject;

public class FilterListViewPresenter extends BasePresenterImpl<FilterListViewView>
        implements FilterListViewContract.Presenter {

    @Inject
    FilterListViewActivity activity;

    @Inject
    Navigator navigator;

    @Inject
    FilterListViewPresenter() {
    }

}
