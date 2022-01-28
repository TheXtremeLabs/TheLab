package com.riders.thelab.ui.mainactivity

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.RowMainAppItemBinding
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class MainActivityAdapter constructor(
    private val mContext: Context,
    private val mAppList: List<App>,
    private val mListener: MainActivityAppClickListener
) : RecyclerView.Adapter<RowAppViewHolder>(), Filterable {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowAppViewHolder {
        val viewBinding: RowMainAppItemBinding =
            RowMainAppItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RowAppViewHolder(mContext, viewBinding, mListener)
    }

    override fun onBindViewHolder(
        holder: RowAppViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val item = appFilteredList!![position]

        if (!LabCompatibilityManager.isTablet(mContext)) {

            /*
             *
             * Reference : https://levelup.gitconnected.com/android-recyclerview-animations-in-kotlin-1e323ffd39be
             *
             */
            // If the bound view wasn't previously displayed on screen, it's animated
            /*if (position > lastPosition) {
                val animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left)
                holder.viewBinding.rowItemCardView?.startAnimation(animation)
                lastPosition = position
            }*/

            holder.bindData(item)
        } else {
            holder.bindTabletData(item)
            bindTabletViewHolder(holder, item, position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun bindTabletViewHolder(holder: RowAppViewHolder, item: App, position: Int) {
        if (position == lastPosition) {
            // Item selected
            holder.viewBinding.cardFrameLayout?.alpha = 1f
            holder.viewBinding.llCardSelectedBackground?.visibility = View.VISIBLE
            holder.viewBinding.cardFrameLayout?.animate()
                ?.setDuration(500)
                ?.scaleX(1.25f)
                ?.scaleY(1.25f)
                ?.start()
            holder.viewBinding.rowItemCardView?.cardElevation = 4f
        } else {
            if (lastPosition == -1) // Check first launch nothing is selected
                holder.viewBinding.cardFrameLayout?.alpha = 1f else {
                // Item not selected
                holder.viewBinding.llCardSelectedBackground?.visibility = View.INVISIBLE
                holder.viewBinding.cardFrameLayout?.alpha = 0.5f
                //                holder.frameLayout.setStrokeWidth((int) 0f);
                holder.viewBinding.cardFrameLayout?.scaleX = 1f
                holder.viewBinding.cardFrameLayout?.scaleY = 1f
                holder.viewBinding.rowItemCardView?.cardElevation = 0f
            }
        }

        holder.viewBinding.rowItemCardView?.setOnClickListener { view: View? ->
            lastPosition = position
            notifyDataSetChanged()
            mListener.onAppItemCLickListener(view!!, item, holder.absoluteAdapterPosition)
        }
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