package com.riders.thelab.ui.filterlistview;

import com.riders.thelab.data.local.model.WorldPopulation;

import java.util.List;

public interface FilterListViewContract {

    interface View extends BaseView {
    }

    interface Presenter {
        List<WorldPopulation> generatePopulationList();
    }
}
