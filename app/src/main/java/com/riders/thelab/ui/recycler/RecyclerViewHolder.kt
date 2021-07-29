package com.riders.thelab.ui.recycler

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.databinding.RowRecyclerViewBinding
import jp.wasabeef.glide.transformations.BlurTransformation
import timber.log.Timber

class RecyclerViewHolder(
    val context: Context, itemView: View
) : RecyclerView.ViewHolder(itemView) {

    val viewBinding: RowRecyclerViewBinding = RowRecyclerViewBinding.bind(itemView)

    private var mItemSelection: Artist? = null
    private var mPosition = 0


    fun bind(artist: Artist) {
        Timber.d(artist.toString())
        if (!LabCompatibilityManager.isTablet(context)) {
            viewBinding.ivBackgroundBlurred.visibility = View.VISIBLE

            //Load the background  thumb image
            Glide.with(context)
                .load(artist.urlThumb)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 3)))
                .into(viewBinding.ivBackgroundBlurred)
        }

        //Load the front image
        Glide.with(context)
            .load(artist.urlThumb)
            .into(viewBinding.transitionImageView)
        viewBinding.rowNameTextView.text = artist.artistName
    }

    fun storeItem(item: Artist, position: Int) {
        Timber.d("storeItem()")
        mItemSelection = item
        mPosition = position
    }
}