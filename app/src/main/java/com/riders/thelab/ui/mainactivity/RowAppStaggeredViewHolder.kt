package com.riders.thelab.ui.mainactivity

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.RowMainAppStaggeredItemBinding

class RowAppStaggeredViewHolder constructor(
    private val context: Context,
    private val itemBinding: RowMainAppStaggeredItemBinding,
    private val mListener: MainActivityAppClickListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    private val viewBinding: RowMainAppStaggeredItemBinding get() = itemBinding

    init {
        itemBinding.run {
            this.listener = mListener
        }
    }

    fun bindData(app: App) {

        viewBinding.app = app

        /*Glide.with(context)
            .load( app.appDrawableIcon)
            .placeholder(R.mipmap.ic_launcher_round)
            .apply {
                dontTransform()
            }
            .into(viewBinding.ivRowAppIcon)*/
    }
}