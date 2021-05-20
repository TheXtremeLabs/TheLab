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
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.data.remote.dto.artist.Artist
import timber.log.Timber
import java.util.function.Predicate

class RecyclerViewAdapter(
    val context: Context,
    artistList: List<Artist>,
    artistThumbnails: List<String>,
    listener: RecyclerClickListener
) : RecyclerView.Adapter<RecyclerViewHolder>() {

    private lateinit var transitionImageView: ShapeableImageView
    private var artistList: List<Artist> = ArrayList()
    private lateinit var artistThumbnails: List<String>
    private lateinit var listener: RecyclerClickListener
    private var mRecyclerView: RecyclerView? = null
    private var mExpandedPosition = -1

    init {
        this.artistList = artistList
        this.artistThumbnails = artistThumbnails
        this.listener = listener
    }

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
                if (artist.urlThumb.let { element.contains(it) }) {
                    artist.urlThumb = element
                }
            }
        } else {
            artist.urlThumb =
                artistThumbnails
                    .stream()
                    .filter { element: String ->
                        element.contains(artist.urlThumb)
                    }
                    ?.findFirst()
                    ?.orElse("").toString()

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
        holder.viewBinding.rowCardView.setOnClickListener { v: View? ->
            listener.onRecyclerClick(artist)

            // if the clicked item is already expanded then return -1
            //else return the position (this works with notifyDataSetChanged )
            mExpandedPosition = if (isExpanded) -1 else position

            //This will call the onBindViewHolder for all the itemViews on Screen
            notifyDataSetChanged()
            holder.storeItem(artist, position)
            transitionImageView = holder.viewBinding.transitionImageView
        }
        holder.viewBinding.rowDetailBtn.setOnClickListener { detailView: View? ->
            Timber.e("setOnClickListener detailView ")
            listener.onDetailClick(
                artist,
                transitionImageView,
                position
            )
        }
        holder.viewBinding.rowDeleteBtn.setOnClickListener { deleteView: View? ->
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