package com.riders.thelab.ui.filterlistview

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.data.local.model.WorldPopulation
import java.util.*
import kotlin.collections.ArrayList


class FilterListViewAdapter(context: Context, worldPopulationList: MutableList<WorldPopulation>) :
    BaseAdapter() {

    private var mWorldPopulationList: MutableList<WorldPopulation>? = worldPopulationList
    private var arraylist: ArrayList<WorldPopulation>? = null
    var mContext: Context? = context
    var mInflater: LayoutInflater? = null

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

        var convertView: View? = convertView
        val holder: ViewHolder

        if (convertView == null) {
            holder = ViewHolder()
            convertView = mInflater?.inflate(R.layout.row_filter_listview_item, null)

            // Locate the TextViews in listview_item.xml
            if (convertView != null) {
                holder.rank = convertView.findViewById(R.id.rank)
                holder.country = convertView.findViewById(R.id.country)
                holder.population = convertView.findViewById(R.id.population)
                convertView.tag = holder
            }
        } else {
            holder = convertView.tag as ViewHolder
        }

        // Set the results into TextViews
        holder.rank!!.text = mWorldPopulationList!![position].rank
        holder.country!!.text = mWorldPopulationList!![position].country
        holder.population!!.text = mWorldPopulationList!![position].population

        // Listen for ListView Item Click
        convertView?.setOnClickListener {
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

        return convertView
    }

    // Filter Class
    fun filter(charText: String) {

        var charText = charText

        charText = charText.toLowerCase(Locale.getDefault())
        mWorldPopulationList!!.clear()

        if (charText.isEmpty()) {
            mWorldPopulationList!!.addAll(arraylist!!)
        } else {
            for (wp in arraylist!!) {
                if (wp.country.toLowerCase(Locale.getDefault()).contains(charText)) {
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