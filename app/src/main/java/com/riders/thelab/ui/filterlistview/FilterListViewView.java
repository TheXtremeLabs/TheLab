package com.riders.thelab.ui.filterlistview;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.WorldPopulation;
import com.riders.thelab.ui.base.BaseViewImpl;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class FilterListViewView extends BaseViewImpl<FilterListViewPresenter>
        implements FilterListViewContract.View, TextWatcher {

    private FilterListViewActivity context;

    @BindView(R.id.lv_filter_listview)
    ListView list;
    @BindView(R.id.et_filter_listview_search)
    EditText filterEditText;

    FilterListViewAdapter adapter;

    ArrayList<WorldPopulation> mWorldPopulationList;

    String[] rank;
    String[] country;
    String[] population;


    @Inject
    FilterListViewView(FilterListViewActivity context) {
        this.context = context;
    }


    @Override
    public void onCreate() {

        getPresenter().attachView(this);

        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_filter_list_view));

        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        setData();

        setAdapter();


        // Capture Text in EditText
        filterEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = filterEditText.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void setData() {
        // Generate sample data
        rank = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        country = new String[]{"China", "India", "United States",
                "Indonesia", "Brazil", "Pakistan", "Nigeria", "Bangladesh",
                "Russia", "Japan"};

        population = new String[]{"1,354,040,000", "1,210,193,422",
                "315,761,000", "237,641,326", "193,946,886", "182,912,000",
                "170,901,000", "152,518,015", "143,369,806", "127,360,000"};

    }

    private void setAdapter() {

        mWorldPopulationList = new ArrayList<>();

        for (int i = 0; i < rank.length; i++) {
            WorldPopulation wp = new WorldPopulation(rank[i], country[i], population[i]);
            // Binds all strings into an array
            mWorldPopulationList.add(wp);
        }

        // Pass results to ListViewAdapter Class
        adapter = new FilterListViewAdapter(context, mWorldPopulationList);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        context = null;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Timber.d("onTextChanged()");
    }

    @Override
    public void afterTextChanged(Editable s) {

        String text = filterEditText.getText().toString().toLowerCase(Locale.getDefault());
        adapter.filter(text);
    }
}
