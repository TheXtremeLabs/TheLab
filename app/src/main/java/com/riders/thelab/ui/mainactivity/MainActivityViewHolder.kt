package com.riders.thelab.ui.mainactivity

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabGlideListener
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.RowMainAppItemBinding

class MainActivityViewHolder constructor(
    private val context: Context,
    val viewBinding: RowMainAppItemBinding
) : RecyclerView.ViewHolder(viewBinding.root) {


    fun bindData(app: App) {

        viewBinding.app = app

        // Load left icon drawable
        viewBinding.rowIconImageView?.let {
            UIManager.loadImage(
                context,
                (if (0 != app.appIcon) app.appIcon else app.appDrawableIcon)!!,
                it,
                LabGlideListener(onLoadingSuccess = { resource: Drawable? ->
                    if (0 != app.appIcon && app.appTitle == "Palette") {
                        val myBitmap = UIManager.drawableToBitmap(resource!!)
                        val newBitmap = UIManager.addGradientToImageView(context, myBitmap)
                        it.setImageDrawable(
                            BitmapDrawable(context.resources, newBitmap)
                        )
                        return@LabGlideListener true
                    }
                    if (0 != app.appIcon && app.appTitle == "WIP") {
                        it.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.logo_testing)
                        )
                        viewBinding.arrowIcon?.visibility = View.GONE
                        return@LabGlideListener true
                    }
                    false
                })
            )
        }

        loadBackgroundImage(app)

    }

    fun bindTabletData(app: App) {
        viewBinding.app = app
        loadBackgroundImage(app)
    }

    private fun loadBackgroundImage(app: App) {
        // Load background image
        UIManager.loadImage(
            context,
            (if (null != app.appActivity) app.appIcon else app.appDrawableIcon)!!,
            viewBinding.ivRowItemBackground
        )
    }
}