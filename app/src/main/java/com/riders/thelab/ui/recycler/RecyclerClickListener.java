package com.riders.thelab.ui.recycler;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.data.remote.dto.artist.Artist;

public interface RecyclerClickListener {
    void onRecyclerClick(Artist artist);

    void onDetailClick(Artist artist, ShapeableImageView sharedImageView, int position);

    void onDeleteClick(Artist artist, int position);
}
