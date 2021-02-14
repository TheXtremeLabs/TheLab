package com.riders.thelab.ui.recycler;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.data.local.model.RecyclerItem;

public interface RecyclerClickListener {
    void onRecyclerClick(RecyclerItem item);

    void onDetailClick(RecyclerItem item, ShapeableImageView sharedImageView, int position);

    void onDeleteClick(RecyclerItem item, int position);
}
