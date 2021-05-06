package com.riders.thelab.ui.youtubelike;

import androidx.annotation.NonNull;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.data.local.model.Video;


public interface YoutubeListClickListener {
    void onYoutubeItemClicked(
            @NonNull ShapeableImageView thumbShapeableImageView,
            @NonNull MaterialTextView titleTextView,
            @NonNull MaterialTextView descriptionTextView,
            Video video,
            int position);
}
