package com.riders.thelab.ui.recycler;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.data.local.model.RecyclerItem;
import com.riders.thelab.data.remote.dto.Artist;

public interface RecyclerClickListener {
    /*void onRecyclerClick(RecyclerItem item);

    void onDetailClick(RecyclerItem item, ShapeableImageView sharedImageView, int position);

    void onDeleteClick(RecyclerItem item, int position);*/


    void onRecyclerClick(Artist artist);

    void onDetailClick(Artist artist, ShapeableImageView sharedImageView, int position);

    void onDeleteClick(Artist artist, int position);
}
