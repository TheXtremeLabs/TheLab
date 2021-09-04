package com.riders.thelab.ui.weather

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.cursoradapter.widget.CursorAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.data.local.model.weather.CityModel
import com.riders.thelab.utils.Constants
import java.util.*

@SuppressLint("RestrictedApi")
class WeatherSearchViewAdapter(
    private val mContext: Context,
    private val mCursor: Cursor,
    private val mSearchView: SearchView,
    private val mListener: WeatherClickListener
) : CursorAdapter(mContext, mCursor, false) {

    private var mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)

    override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View? {
        return mLayoutInflater.inflate(R.layout.row_city_spinner, parent, false)
    }

    @SuppressLint("RestrictedApi")
    override fun bindView(view: View, context: Context, cursor: Cursor) {

        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        val country = cursor.getString(cursor.getColumnIndexOrThrow("country"))

        val countryURL: String =
            (Constants.BASE_ENDPOINT_WEATHER_FLAG
                    + country.lowercase()
                    + Constants.WEATHER_FLAG_PNG_SUFFIX)

        val tvCityName: MaterialTextView = view.findViewById(R.id.row_tv_city_name)
        val tvCityCountry: MaterialTextView = view.findViewById(R.id.row_tv_city_country)
        val ivCityFlag: ShapeableImageView = view.findViewById(R.id.row_iv_city_flag)

        tvCityName.text = name
        tvCityCountry.text = country
        Glide.with(mContext)
            .load(countryURL)
            .into(ivCityFlag)

        view.setOnClickListener { view1: View? ->
            //take next action based user selected item
            if (!LabCompatibilityManager.isOreo()) {
                if (!mSearchView.isIconified) {
                    mSearchView.isIconified = true
                }
            } else {
                Objects.requireNonNull((context as WeatherActivity).supportActionBar)
                    ?.collapseActionView()
            }
            mListener.onWeatherItemClicked(CityModel(cursor))
        }
    }
}