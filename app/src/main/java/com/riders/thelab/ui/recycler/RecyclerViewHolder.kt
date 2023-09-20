package com.riders.thelab.ui.recycler

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.remote.dto.artist.Artist
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.databinding.RowRecyclerViewBinding
import timber.log.Timber

class RecyclerViewHolder(
    val context: Context, itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val viewBinding: RowRecyclerViewBinding = RowRecyclerViewBinding.bind(itemView)

    private var mItemSelection: Artist? = null
    private var mPosition = 0

    fun bind(artist: Artist) {
        viewBinding.artist = artist

        if (!LabCompatibilityManager.isTablet(context)) {
            viewBinding.ivBackgroundBlurred.visibility = View.VISIBLE
            //Load the background  thumb image
            UIManager.loadImageBlurred(context, artist.urlThumb, viewBinding.ivBackgroundBlurred)
        }

        //Load the front image
        UIManager.loadImage(context, artist.urlThumb, viewBinding.transitionImageView)
    }

    fun storeItem(item: Artist, position: Int) {
        Timber.d("storeItem()")
        mItemSelection = item
        mPosition = position
    }
}