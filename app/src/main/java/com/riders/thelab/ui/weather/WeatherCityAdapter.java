package com.riders.thelab.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.weather.CityModel;

import java.util.ArrayList;
import java.util.List;

public class WeatherCityAdapter extends ArrayAdapter<CityModel> {

    private final Context context;
    private final int viewResourceId;
    private final List<CityModel> items;
    private final List<CityModel> itemsAll;
    private final List<CityModel> suggestions;

    private final Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((CityModel) (resultValue)).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CityModel City : itemsAll) {
                    if (City.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(City);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<CityModel> filteredList = (ArrayList<CityModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CityModel c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };


    public WeatherCityAdapter(@NonNull Context context,
                              int viewResourceId,
                              @NonNull ArrayList<CityModel> items) {
        super(context, viewResourceId, items);

        this.context = context;

        this.items = items;
        this.itemsAll = (List<CityModel>) items.clone();
        this.suggestions = new ArrayList<>();
        this.viewResourceId = viewResourceId;
    }

    @Override
    public int getCount() {
        if (null != items)
            return items.size();
        return 0;
    }

    @Nullable
    @Override
    public CityModel getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void populateItem(CityModel item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(viewResourceId, null);
        }

        CityModel cityItem = items.get(position);

        if (cityItem != null) {
            MaterialTextView tvCityName = view.findViewById(R.id.row_city_name_textView);
            if (tvCityName != null) {

                String szCity = items.get(position).getName() +
                        context.getResources().getString(R.string.separator_placeholder) +
                        items.get(position).getCountry();

                tvCityName.setText(szCity);
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }
}
