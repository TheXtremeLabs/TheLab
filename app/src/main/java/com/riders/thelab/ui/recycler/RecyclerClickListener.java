package com.riders.thelab.ui.recycler;

import android.widget.ImageView;

import com.riders.thelab.data.local.model.RecyclerItem;

public interface RecyclerClickListener {
    void onRecyclerClick(RecyclerItem item);

    void onDetailClick(RecyclerItem item, ImageView sharedImageView, int position);

    void onDeleteClick(RecyclerItem item, int position);
}
