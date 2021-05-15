package com.riders.thelab.ui.weather;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.CursorAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.weather.CityModel;
import com.riders.thelab.utils.Constants;

public class WeatherSearchViewAdapter extends CursorAdapter {

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SearchView mSearchView;
    private WeatherClickListener mListener;

    public WeatherSearchViewAdapter(Context context,
                                    Cursor cursor,
                                    final SearchView searchView,
                                    WeatherClickListener listener) {
        super(context, cursor, false);
        mContext = context;
        mSearchView = searchView;
        mLayoutInflater = LayoutInflater.from(context);
        mListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.row_city_spinner, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));

        String countryURL =
                Constants.BASE_ENDPOINT_WEATHER_FLAG
                        + country.toLowerCase()
                        + Constants.WEATHER_FLAG_PNG_SUFFIX;

        MaterialTextView tvCityName = view.findViewById(R.id.row_tv_city_name);
        MaterialTextView tvCityCountry = view.findViewById(R.id.row_tv_city_country);
        ShapeableImageView ivCityFlag = view.findViewById(R.id.row_iv_city_flag);

        if (tvCityName != null)
            tvCityName.setText(name);

        if (tvCityCountry != null)
            tvCityCountry.setText(country);

        if (ivCityFlag != null)
            Glide.with(mContext)
                    .load(countryURL)
                    .into(ivCityFlag);

        view.setOnClickListener(view1 -> {
            //take next action based user selected item
            mSearchView.setIconified(true);
            assert tvCityName != null;
            Toast.makeText(context, "Selected suggestion " + tvCityName.getText(), Toast.LENGTH_LONG).show();

            mListener.onWeatherItemClicked(new CityModel(cursor));
        });
    }
}
