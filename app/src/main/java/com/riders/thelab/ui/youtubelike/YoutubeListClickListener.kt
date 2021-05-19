package com.riders.thelab.ui.youtubelike

import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.data.local.model.Video

interface YoutubeListClickListener {

    fun onYoutubeItemClicked(
        thumbShapeableImageView: ShapeableImageView,
        titleTextView: MaterialTextView,
        descriptionTextView: MaterialTextView,
        video: Video,
        position: Int
    )

}