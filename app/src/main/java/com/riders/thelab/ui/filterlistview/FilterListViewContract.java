package com.riders.thelab.ui.filterlistview;

import com.riders.thelab.data.local.model.WorldPopulation;
import com.riders.thelab.ui.base.BaseView;

import java.util.List;

public interface FilterListViewContract {

    interface View extends BaseView {
    }

    interface Presenter {
        List<WorldPopulation> generatePopulationList();
    }
}
