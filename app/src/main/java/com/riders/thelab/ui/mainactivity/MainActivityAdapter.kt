package com.riders.thelab.ui.mainactivity

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.data.local.model.App

class MainActivityAdapter constructor(
    context: Context,
    appList: List<App>,
    listener: MainActivityAppClickListener
) : RecyclerView.Adapter<MainActivityViewHolder>() {

    private val mContext: Context = context
    private val mAppList: List<App> = appList
    private val mListener: MainActivityAppClickListener = listener

    // Allows to remember the last item shown on screen
    private var lastPosition = -1

    override fun getItemCount(): Int {
        return mAppList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainActivityViewHolder {
        return MainActivityViewHolder(
            mContext,
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_main_app_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainActivityViewHolder, position: Int) {
        val item = mAppList[position]

        if (!LabCompatibilityManager.isTablet(mContext)) {

            /*
             *
             * Reference : https://levelup.gitconnected.com/android-recyclerview-animations-in-kotlin-1e323ffd39be
             *
             * */
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                val animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left)
                holder.viewBinding.rowItemCardView.startAnimation(animation)
                lastPosition = position
            }
            holder.bindData(item)
            holder.viewBinding.rowItemCardView.setOnClickListener { view: View? ->
                mListener.onAppItemCLickListener(
                    view!!,
                    item,
                    position
                )
            }
        } else {
            holder.bindTabletData(item)
            bindTabletViewHolder(holder, item, position)
        }
    }

    private fun bindTabletViewHolder(holder: MainActivityViewHolder, item: App, position: Int) {
        if (position == lastPosition) {
            // Item selected
            holder.viewBinding.cardFrameLayout.alpha = 1f
            holder.viewBinding.llCardSelectedBackground?.visibility = View.VISIBLE
            holder.viewBinding.cardFrameLayout.animate()
                .setDuration(500)
                .scaleX(1.25f)
                .scaleY(1.25f)
                .start()
            holder.viewBinding.rowItemCardView.cardElevation = 4f
        } else {
            if (lastPosition == -1) // Check first launch nothing is selected
                holder.viewBinding.cardFrameLayout.alpha = 1f else {
                // Item not selected
                holder.viewBinding.llCardSelectedBackground?.visibility = View.INVISIBLE
                holder.viewBinding.cardFrameLayout.alpha = 0.5f
                //                holder.frameLayout.setStrokeWidth((int) 0f);
                holder.viewBinding.cardFrameLayout.scaleX = 1f
                holder.viewBinding.cardFrameLayout.scaleY = 1f
                holder.viewBinding.rowItemCardView.cardElevation = 0f
            }
        }
        holder.viewBinding.rowItemCardView
            .setOnClickListener { view: View? ->
                lastPosition = position
                notifyDataSetChanged()
                mListener.onAppItemCLickListener(view!!, item, holder.adapterPosition)
            }
    }

}