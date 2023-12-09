package com.riders.thelab.ui.filterlistview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.WorldPopulation


class FilterListViewAdapter(context: Context, worldPopulationList: MutableList<WorldPopulation>) :
    BaseAdapter() {

    private var mWorldPopulationList: MutableList<WorldPopulation>? = worldPopulationList
    private var arraylist: ArrayList<WorldPopulation>? = null
    private var mContext: Context? = context
    private var mInflater: LayoutInflater? = null

    init {
        mInflater = LayoutInflater.from(mContext)
        arraylist = ArrayList()
        arraylist!!.addAll(worldPopulationList)
    }


    override fun getCount(): Int {
        return if (mWorldPopulationList!!.isNotEmpty()) mWorldPopulationList!!.size else 0
    }

    override fun getItem(position: Int): WorldPopulation {
        return mWorldPopulationList!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

        var mConvertView: View? = convertView
        var holder: ViewHolder? = null

        if (convertView == null) {
            holder = ViewHolder()
            mConvertView = mInflater?.inflate(R.layout.row_filter_listview_item, null)

            // Locate the TextViews in listview_item.xml
            if (mConvertView != null) {
                holder.rank = mConvertView.findViewById(R.id.rank)
                holder.country = mConvertView.findViewById(R.id.country)
                holder.population = mConvertView.findViewById(R.id.population)
                mConvertView.tag = holder
            }
        } else {
            if (mConvertView != null) {
                holder = mConvertView.tag as ViewHolder
            }
        }

        // Set the results into TextViews
        holder?.rank!!.text = mWorldPopulationList!![position].rank
        holder.country!!.text = mWorldPopulationList!![position].country
        holder.population!!.text = mWorldPopulationList!![position].population

        // Listen for ListView Item Click
        mConvertView?.setOnClickListener {
            // Send single item click data to SingleItemView Class
            val intent = Intent(
                mContext,
                FilterListViewDetailActivity::class.java
            )
            // Pass all data rank
            intent.putExtra("rank", mWorldPopulationList!![position].rank)
            // Pass all data country
            intent.putExtra("country", mWorldPopulationList!![position].country)
            // Pass all data population
            intent.putExtra("population", mWorldPopulationList!![position].population)
            // Pass all data flag
            // Start SingleItemView Class
            mContext?.startActivity(intent)
        }

        return mConvertView
    }

    // Filter Class
    fun filter(filterText: String) {

        var charText = filterText

        charText = charText.lowercase()
        mWorldPopulationList!!.clear()

        if (charText.isEmpty()) {
            mWorldPopulationList!!.addAll(arraylist!!)
        } else {
            for (wp in arraylist!!) {
                if (wp.country.lowercase().contains(charText)) {
                    mWorldPopulationList!!.add(wp)
                }
            }
        }

        notifyDataSetChanged()
    }

    class ViewHolder {
        var rank: MaterialTextView? = null
        var country: MaterialTextView? = null
        var population: MaterialTextView? = null
    }

}