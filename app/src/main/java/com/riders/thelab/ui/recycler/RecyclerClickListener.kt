package com.riders.thelab.ui.recycler

import com.google.android.material.imageview.ShapeableImageView
import com.riders.thelab.data.remote.dto.artist.Artist

interface RecyclerClickListener {
    fun onRecyclerClick(artist: Artist)

    fun onDetailClick(artist: Artist, sharedImageView: ShapeableImageView, position: Int)

    fun onDeleteClick(artist: Artist, position: Int)
}