package com.riders.thelab.ui.recycler

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.imageview.ShapeableImageView
import com.riders.thelab.R
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.remote.dto.artist.Artist
import timber.log.Timber

class RecyclerViewAdapter(
    private val context: Context,
    private val artistList: List<Artist>,
    private val artistThumbnails: List<String>,
    private val listener: RecyclerClickListener
) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private lateinit var transitionImageView: ShapeableImageView
    private var mRecyclerView: RecyclerView? = null
    private var mExpandedPosition = -1

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.mRecyclerView = recyclerView

        // fancy animations can skip if like
        TransitionManager.beginDelayedTransition(recyclerView)
    }

    override fun getItemCount(): Int {
        return artistList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(
            context,
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.row_recycler_view, parent, false)
        )
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val artist: Artist = artistList[position]

        if (!LabCompatibilityManager.isNougat()) {
            for (element in artistThumbnails) {
                /*if (artist.urlThumb.let { element.contains(it) }) {
                    artist.urlThumb = element
                }*/
            }
        } else {
            /*artist.urlThumb =
                artistThumbnails
                    .stream()
                    .filter { element: String ->
                        element.contains(artist.urlThumb)
                    }
                    ?.findFirst()
                    ?.orElse("").toString()*/

        }
        holder.bind(artist)

        // This line checks if the item displayed on screen
        // was expanded or not (Remembering the fact that Recycler View )
        // reuses views so onBindViewHolder will be called for all
        // items visible on screen.
        val isExpanded = position == mExpandedPosition

        //This line hides or shows the layout in question
        holder.viewBinding.rowDetailsLinearLayout.visibility =
            if (isExpanded) View.VISIBLE else View.GONE

        // I do not know what the heck this is :)
        holder.itemView.isActivated = isExpanded

        // Click event for each item (itemView is an in-built variable of holder class)
        holder.viewBinding.rowCardView.setOnClickListener {
            listener.onRecyclerClick(artist)

            // if the clicked item is already expanded then return -1
            //else return the position (this works with notifyDataSetChanged )
            mExpandedPosition = if (isExpanded) -1 else position

            //This will call the onBindViewHolder for all the itemViews on Screen
            notifyDataSetChanged()
            holder.storeItem(artist, position)
            transitionImageView = holder.viewBinding.transitionImageView
        }
        holder.viewBinding.rowDetailBtn.setOnClickListener {
            Timber.e("setOnClickListener detailView ")
            listener.onDetailClick(
                artist,
                transitionImageView,
                position
            )
        }
        holder.viewBinding.rowDeleteBtn.setOnClickListener {
            Timber.e("setOnClickListener deleteView ")
            listener.onDeleteClick(artist, position)
        }
    }

    fun removeItem(position: Int) {
        (artistList as ArrayList).removeAt(position)
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position)
    }

    fun restoreItem(item: Artist, position: Int) {
        (artistList as ArrayList).add(position, item)
        // notify item added by position
        notifyItemInserted(position)
    }
}