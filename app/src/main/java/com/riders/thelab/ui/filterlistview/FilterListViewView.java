package com.riders.thelab.ui.filterlistview;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.WorldPopulation;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class FilterListViewView extends BaseViewImpl<FilterListViewPresenter>
        implements FilterListViewContract.View, TextWatcher {

    @BindView(R.id.lv_filter_listview)
    ListView list;
    @BindView(R.id.et_filter_listview_search)
    EditText filterEditText;
    FilterListViewAdapter adapter;
    ArrayList<WorldPopulation> mWorldPopulationList;
    private FilterListViewActivity context;


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

        setAdapter();

        // Capture Text in EditText
        filterEditText.addTextChangedListener(this);
    }

    private void setAdapter() {

        mWorldPopulationList =
                (ArrayList<WorldPopulation>) getPresenter().generatePopulationList();

        // Pass results to ListViewAdapter Class
        adapter = new FilterListViewAdapter(context, mWorldPopulationList);

        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        getPresenter().detachView();

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
