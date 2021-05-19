package com.riders.thelab.ui.filterlistview;

import com.riders.thelab.data.local.model.WorldPopulation;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FilterListViewPresenter extends BasePresenterImpl<FilterListViewView>
        implements FilterListViewContract.Presenter {

    @Inject
    FilterListViewActivity activity;

    @Inject
    Navigator navigator;

    String[] rank;
    String[] country;
    String[] population;


    @Inject
    FilterListViewPresenter() {

        // Generate sample data
        rank = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        country = new String[]{"China", "India", "United States",
                "Indonesia", "Brazil", "Pakistan", "Nigeria", "Bangladesh",
                "Russia", "Japan"};

        population = new String[]{"1,354,040,000", "1,210,193,422",
                "315,761,000", "237,641,326", "193,946,886", "182,912,000",
                "170,901,000", "152,518,015", "143,369,806", "127,360,000"};
    }

    @Override
    public List<WorldPopulation> generatePopulationList() {

        List<WorldPopulation> list = new ArrayList<>();

        for (int i = 0; i < rank.length; i++) {
            WorldPopulation wp = new WorldPopulation(rank[i], country[i], population[i]);
            // Binds all strings into an array
            list.add(wp);
        }

        return list;
    }
}
