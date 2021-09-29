package com.riders.thelab.ui.mainactivity

import android.content.Context
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.RowMainAppItemBinding

class RowAppViewHolder constructor(
    private val context: Context,
    private val itemBinding: RowMainAppItemBinding,
    private val mListener: MainActivityAppClickListener
) : RecyclerView.ViewHolder(itemBinding.root) {

    val viewBinding: RowMainAppItemBinding get() = itemBinding

    init {
        itemBinding.run {
            this.listener = mListener
        }
    }

    fun bindData(app: App) {
        viewBinding.app = app
    }

    fun bindTabletData(app: App) {
        viewBinding.ivRowItemBackground?.let { loadBackgroundImage(context, app, it) }
    }


    private fun loadBackgroundImage(context: Context, app: App, view: ImageView) {
        // Load background image
        UIManager.loadImage(
            context,
            app.appDrawableIcon,
            view as ShapeableImageView
        )
    }
}