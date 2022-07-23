package com.riders.thelab.ui.mainactivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.RowMainAppStaggeredItemBinding
import timber.log.Timber
import java.util.*

class MainActivityStaggeredAdapter constructor(
    private val mContext: Context,
    private val mAppList: List<App>,
    private val mListener: MainActivityAppClickListener
) : RecyclerView.Adapter<RowAppStaggeredViewHolder>(), Filterable {

    private var appFilteredList: MutableList<App>? = mAppList as MutableList<App>

    // Allows to remember the last item shown on screen
    private var lastPosition = -1

    override fun getItemId(position: Int): Long {
        return appFilteredList
            ?.get(position)
            ?.id!!
    }

    override fun getItemCount(): Int {
        return if (null != appFilteredList) {
            appFilteredList!!.size
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowAppStaggeredViewHolder {
        val viewBinding: RowMainAppStaggeredItemBinding =
            RowMainAppStaggeredItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return RowAppStaggeredViewHolder(mContext, viewBinding, mListener)
    }

    override fun onBindViewHolder(
        holder: RowAppStaggeredViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val item = appFilteredList!![position]

        holder.bindData(item)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                Timber.d("performFiltering()")
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    Timber.d("charString.isEmpty()")
                    appFilteredList = mAppList as MutableList<App>
                } else {
                    val filteredList: MutableList<App> = ArrayList()

                    for (row in mAppList) {
                        Timber.d("row : %s", row.toString())

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.appName?.lowercase(Locale.ROOT)
                                ?.contains(charString.lowercase(Locale.ROOT)) == true
                            || row.appTitle?.lowercase(Locale.ROOT)
                                ?.contains(charString.lowercase(Locale.ROOT)) == true
                        ) {
                            filteredList.add(row)
                        }
                    }

                    appFilteredList = filteredList
                    Timber.d("contactListFiltered size : %s", appFilteredList!!.size)
                }
                val filterResults = FilterResults()
                filterResults.values = appFilteredList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                Timber.d("publishResults()")
                appFilteredList = filterResults.values as MutableList<App>?

                // refresh the list with filtered data
                notifyDataSetChanged()
            }
        }
    }

}