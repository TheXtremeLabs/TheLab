package com.riders.thelab.ui.filterlistview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.WorldPopulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterListViewAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    private List<WorldPopulation> mWorldPopulationList;
    private ArrayList<WorldPopulation> arraylist;

    public FilterListViewAdapter(Context context, List<WorldPopulation> worldPopulationList) {
        mContext = context;
        this.mWorldPopulationList = worldPopulationList;
        mInflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<WorldPopulation>();
        this.arraylist.addAll(worldPopulationList);
    }


    @Override
    public int getCount() {
        if (mWorldPopulationList.size() != 0)
            return mWorldPopulationList.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mWorldPopulationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.row_filter_listview_item, null);
            // Locate the TextViews in listview_item.xml
            holder.rank = convertView.findViewById(R.id.rank);
            holder.country = convertView.findViewById(R.id.country);
            holder.population = convertView.findViewById(R.id.population);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Set the results into TextViews
        holder.rank.setText(mWorldPopulationList.get(position).getRank());
        holder.country.setText(mWorldPopulationList.get(position).getCountry());
        holder.population.setText(mWorldPopulationList.get(position).getPopulation());

        // Listen for ListView Item Click
        convertView.setOnClickListener(arg0 -> {

            // Send single item click data to SingleItemView Class
            Intent intent = new Intent(mContext, FilterListViewDetailActivity.class);
            // Pass all data rank
            intent.putExtra("rank", (mWorldPopulationList.get(position).getRank()));
            // Pass all data country
            intent.putExtra("country", (mWorldPopulationList.get(position).getCountry()));
            // Pass all data population
            intent.putExtra("population", (mWorldPopulationList.get(position).getPopulation()));
            // Pass all data flag
            // Start SingleItemView Class
            mContext.startActivity(intent);
        });

        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mWorldPopulationList.clear();
        if (charText.length() == 0) {
            mWorldPopulationList.addAll(arraylist);
        } else {
            for (WorldPopulation wp : arraylist) {
                if (wp.getCountry().toLowerCase(Locale.getDefault()).contains(charText)) {
                    mWorldPopulationList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        MaterialTextView rank;
        MaterialTextView country;
        MaterialTextView population;
    }

}
