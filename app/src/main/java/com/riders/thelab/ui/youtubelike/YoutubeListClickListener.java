package com.riders.thelab.ui.youtubelike;

;

import androidx.annotation.NonNull;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.data.local.model.Video;


public interface YoutubeListClickListener {
    void onYoutubeItemClicked(@NonNull ShapeableImageView view, Video video, int position);
}
