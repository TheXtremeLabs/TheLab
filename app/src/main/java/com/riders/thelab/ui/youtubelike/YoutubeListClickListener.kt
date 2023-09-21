package com.riders.thelab.ui.youtubelike

import android.view.View
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.core.data.local.model.Video

interface YoutubeListClickListener {

    fun onYoutubeItemClicked(view: View, video: Video)

    fun onYoutubeItemClicked(
        thumbShapeableImageView: ShapeableImageView,
        titleTextView: MaterialTextView,
        descriptionTextView: MaterialTextView,
        video: Video,
        position: Int
    )
}