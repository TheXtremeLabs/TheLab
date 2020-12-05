package com.riders.thelab.ui.youtubelike;

import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.riders.thelab.data.local.model.Video;


public interface YoutubeListClickListener {
    void onYoutubeItemClicked(@NonNull ImageView view, Video video, int position);
}
