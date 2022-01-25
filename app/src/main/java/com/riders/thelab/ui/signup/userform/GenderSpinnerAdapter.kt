package com.riders.thelab.ui.signup.userform

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class GenderSpinnerAdapter(
    context: Context,
    layoutResId: Int,
    val itemList: List<String>
) : ArrayAdapter<String>(context, layoutResId, itemList) {

    private val genderList get() = itemList

    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    override fun getCount(): Int {
        return genderList.size
    }

    override fun getItem(position: Int): String {
        return genderList[position]
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getViewTypeCount(): Int {
        return super.getViewTypeCount()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)

        val tv = view as TextView
        if (position == 0) {
            // Set the hint text color gray
            tv.setTextColor(Color.GRAY)
        } else {
            tv.setTextColor(Color.WHITE)
        }
        return view
    }

    override fun setDropDownViewResource(resource: Int) {
        super.setDropDownViewResource(resource)
    }
}