package com.riders.thelab.ui.mainactivity

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.riders.thelab.R
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.RowMainAppItemBinding
import com.riders.thelab.utils.Validator

class MainActivityViewHolder constructor(
    private val context: Context,
    val viewBinding: RowMainAppItemBinding
) : RecyclerView.ViewHolder(viewBinding.root) {


    fun bindData(app: App) {
        // Load left icon drawable
        viewBinding.rowIconImageView?.let {
            Glide.with(context)
                .load(if (0 != app.appIcon) app.appIcon else app.appDrawableIcon)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?, model: Any,
                        target: Target<Drawable>, isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable, model: Any,
                        target: Target<Drawable>, dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (0 != app.appIcon && app.appTitle == "Palette") {
                            val myBitmap = (resource as BitmapDrawable).bitmap
                            val newBitmap = UIManager.addGradientToImageView(context, myBitmap)
                            it.setImageDrawable(
                                BitmapDrawable(context.resources, newBitmap)
                            )
                            return true
                        }
                        if (0 != app.appIcon && app.appTitle == "WIP") {
                            it.setImageDrawable(
                                ContextCompat.getDrawable(context, R.drawable.logo_testing)
                            )
                            viewBinding.arrowIcon?.visibility = View.GONE
                            return true
                        }
                        return false
                    }
                })
                .into(it)
        }


        bindTitleAndDescription(app)

        // Load background image
        Glide.with(context)
            .load(if (null != app.appActivity) app.appIcon else app.appDrawableIcon)
            .into(viewBinding.ivRowItemBackground)

    }

    fun bindTabletData(app: App) {
        // Load background image
        Glide.with(context)
            .load(if (null != app.appActivity) app.appIcon else app.appDrawableIcon)
            .into(viewBinding.ivRowItemBackground)

        bindTitleAndDescription(app)
    }

    private fun bindTitleAndDescription(app: App) {
        viewBinding.rowTitleTextView.text =
            if (!Validator.isEmpty(app.appTitle)) app.appTitle else app.appName
        viewBinding.rowDescriptionTextView.text =
            if (!Validator.isEmpty(app.appVersion)) app.appVersion else app.appDescription
    }
}