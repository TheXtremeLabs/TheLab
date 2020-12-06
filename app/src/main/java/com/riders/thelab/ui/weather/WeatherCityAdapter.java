package com.riders.thelab.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.weather.City;

import java.util.ArrayList;
import java.util.List;

/*
public class WeatherCityAdapter extends RecyclerView.Adapter<WeatherCityViewHolder>
        implements Filterable {

    private Context context;

    private List<City> mCityList;
    private List<City> mCityListFiltered;

    private CitiesAdapterListener listener;

    public interface CitiesAdapterListener {
        void onCitySelected(City city);
    }


    public WeatherCityAdapter(Context context, List<City> cityList, CitiesAdapterListener listener) {
        sortList(cityList);

        this.context = context;
        this.mCityList = cityList;
        this.mCityListFiltered = cityList;
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        if (mCityListFiltered != null) {
            return mCityListFiltered.size();
        }
        return 0;
    }


    @NonNull
    @Override
    public WeatherCityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherCityViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_city_spinner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherCityViewHolder holder, final int position) {

        City city = mCityListFiltered.get(position);

        holder.bindName(city.getName() + "," + city.getCountry());

        holder.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send selected contact in callback
                listener.onCitySelected(mCityListFiltered.get(position));
            }
        });

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mCityListFiltered = mCityList;
                } else {
                    List<City> filteredList = new ArrayList<>();
                    for (City row : mCityList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    mCityListFiltered = filteredList;
                }

                // Sort by name
                sortList(mCityListFiltered);

                FilterResults filterResults = new FilterResults();
                filterResults.values = mCityListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mCityListFiltered = (ArrayList<City>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    private void sortList(List<City> list) {
        Collections.sort(list, new Comparator<City>() {
            @Override
            public int compare(City leftItem, City rightItem) {
                return leftItem.getName().compareTo(rightItem.getName());
            }
        });
    }
}
*/

/*
FOR SPINNER
*/
public class WeatherCityAdapter extends ArrayAdapter<City> {

    private final Context context;
    private final int viewResourceId;
    private final List<City> items;
    private final List<City> itemsAll;
    private final List<City> suggestions;

    private final Filter mFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            return ((City) (resultValue)).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (City City : itemsAll) {
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
            ArrayList<City> filteredList = (ArrayList<City>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (City c : filteredList) {
                    add(c);
                }
                notifyDataSetChanged();
            }
        }
    };


    public WeatherCityAdapter(@NonNull Context context, int viewResourceId, @NonNull List<City> items) {
        super(context, viewResourceId, items);

        this.context = context;

        this.items = items;
        this.itemsAll = items;
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
    public City getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void populateItem(City item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(viewResourceId, null);
        }

        City cityItem = items.get(position);

        if (cityItem != null) {
            TextView tvCityName = view.findViewById(R.id.row_city_name_textView);
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
