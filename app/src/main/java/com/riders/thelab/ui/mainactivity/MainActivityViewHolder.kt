package com.riders.thelab.ui.mainactivity

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
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

        // Load left icon drawable
        app.appDrawableIcon?.let { icon ->
            viewBinding.rowIconImageView?.let { targetView ->
                UIManager.loadImage(
                    context,
                    icon,
                    targetView,
                    LabGlideListener(onLoadingSuccess = { resource: Drawable? ->
                        if (app.appTitle == "Palette") {
                            val myBitmap = UIManager.drawableToBitmap(resource!!)
                            val newBitmap = UIManager.addGradientToImageView(context, myBitmap)
                            targetView.setImageDrawable(
                                BitmapDrawable(context.resources, newBitmap)
                            )
                            return@LabGlideListener true
                        }
                        if (app.appTitle == "WIP") {
                            targetView.setImageDrawable(
                                ContextCompat.getDrawable(context, R.drawable.logo_testing)
                            )
                            viewBinding.arrowIcon?.visibility = View.GONE
                            return@LabGlideListener true
                        }
                        false
                    })
                )
            }
        }

        loadBackgroundImage(context, app, viewBinding.ivRowItemBackground)
    }

    fun bindTabletData(app: App) {
        loadBackgroundImage(context, app, viewBinding.ivRowItemBackground)
    }


    private fun loadBackgroundImage(context: Context, app: App, view: ImageView) {
        // Load background image
        UIManager.loadImage(
            context,
            app.appDrawableIcon!!,
            view as ShapeableImageView
        )
    }
}