package com.riders.thelab.core.ui.utils

import android.content.Context
import android.graphics.drawable.Drawable
//import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
//import com.riders.thelab.TheLabApplication
import jp.wasabeef.glide.transformations.BlurTransformation

/*@BindingAdapter("imageResource", "error", requireAll = false)
fun loadImage(
    targetImageView: ShapeableImageView,
    iconResDrawable: Drawable?,
    error: Drawable,
) {
    if (null != iconResDrawable) {
        //Load the background  thumb image
        Glide.with(TheLabApplication.getInstance().getContext())
            .load(iconResDrawable)
            .error(error)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }
}

@BindingAdapter("imageUrl", "error", requireAll = false)
fun loadImage(
    targetImageView: ShapeableImageView,
    imageUrl: String?,
    error: Drawable,
) {
    if (null != imageUrl) {
        //Load the background  thumb image
        Glide.with(TheLabApplication.getInstance().getContext())
            .load(imageUrl)
            .error(error)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }
}

@BindingAdapter("imageUrl", "error", requireAll = false)
fun loadImage(
    targetImageView: ShapeableImageView,
    imageUrl: String?,
    error: Int,
) {
    if (null != imageUrl) {
        //Load the background  thumb image
        Glide.with(TheLabApplication.getInstance().getContext())
            .load(imageUrl)
            .error(error)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }
}*/

class LabGlideUtils {

    companion object {
        private var INSTANCE: LabGlideUtils? = null

        fun getInstance(): LabGlideUtils {
            if (null == INSTANCE)
                INSTANCE = LabGlideUtils()

            return INSTANCE as LabGlideUtils
        }
    }

    /*fun loadImage(
        targetImageView: ShapeableImageView,
        iconIntRes: Int,
        error: Drawable,
    ) {
        //Load the background  thumb image
        Glide.with(TheLabApplication.getInstance().getContext())
            .load(iconIntRes)
            .error(error)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }

    fun loadImage(
        targetImageView: ShapeableImageView,
        iconIntVectorRes: Int,
        error: Drawable,
        context: Context
    ) {
        //Load the background  thumb image
        Glide.with(TheLabApplication.getInstance().getContext())
            .load(iconIntVectorRes)
            .error(error)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }

    fun loadImage(
        targetImageView: ShapeableImageView,
        iconResDrawable: Drawable,
        error: Drawable,
    ) {
        //Load the background  thumb image
        Glide.with(TheLabApplication.getInstance().getContext())
            .load(iconResDrawable)
            .error(error)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }*/

    fun loadImage(
        context: Context,
        iconResDrawable: Any,
        targetImageView: ShapeableImageView
    ) {
        //Load the background  thumb image
        Glide.with(context)
            .load(iconResDrawable)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }

    fun loadImage(
        context: Context,
        iconResDrawable: Any,
        targetImageView: ShapeableImageView,
        listener: RequestListener<Drawable>
    ) {
        //Load the background  thumb image
        Glide.with(context)
            .load(iconResDrawable)
            .listener(listener)
            .apply {
                dontTransform()
            }
            .into(targetImageView)
    }

    /**
     * Load Image in view and apply a blurry effect
     */
    fun loadImageBlurred(
        context: Context,
        imageURL: Any,
        targetImageView: ShapeableImageView
    ) {
        //Load the background  thumb image
        Glide.with(context)
            .load(imageURL)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 5)))
            .into(targetImageView)
    }
}