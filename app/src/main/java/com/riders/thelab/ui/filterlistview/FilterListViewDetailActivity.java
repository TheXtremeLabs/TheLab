package com.riders.thelab.ui.filterlistview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;


import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class FilterListViewDetailActivity extends SimpleActivity {

    @BindView(R.id.rank)
    MaterialTextView tvRank;
    @BindView(R.id.country)
    MaterialTextView tvCountry;
    @BindView(R.id.population)
    MaterialTextView tvPopulation;

    String rank;
    String country;
    String population;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_filter_listview_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.activity_title_filter_list_view_detail));

        ButterKnife.bind(this, findViewById(android.R.id.content));

        getBundle();

        loadData();
    }

    private void getBundle() {

        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        // Get the results of rank
        rank = i.getStringExtra("rank");
        // Get the results of country
        country = i.getStringExtra("country");
        // Get the results of population
        population = i.getStringExtra("population");

    }

    private void loadData() {

        // Load the results into the TextViews
        tvRank.setText(rank);
        tvCountry.setText(country);
        tvPopulation.setText(population);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
