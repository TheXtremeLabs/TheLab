package com.riders.thelab.ui.mainactivity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.RowWhatSNewItemBinding

class WhatsNewAdapter constructor(
    private val mContext: Context,
    private val mRecentAppList: List<App>,
    private val mListener: MainActivityAppClickListener
) : RecyclerView.Adapter<RowWhatsNewViewHolder>() {

    override fun getItemCount(): Int {
        return mRecentAppList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowWhatsNewViewHolder {
        val viewBinding: RowWhatSNewItemBinding =
            RowWhatSNewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RowWhatsNewViewHolder(mContext, viewBinding, mListener)
    }

    override fun onBindViewHolder(holder: RowWhatsNewViewHolder, position: Int) {
        holder.bindData(mRecentAppList[position])
    }
}