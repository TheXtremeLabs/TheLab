package com.riders.thelab.ui.youtubelike

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.databinding.RowYoutubeLikeItemBinding

class YoutubeLikeListAdapter(
    private val context: Context,
    private val youtubeList: List<Video>,
    private val listener: YoutubeListClickListener
) : RecyclerView.Adapter<YoutubeLikeViewHolder>() {


    // Allows to remember the last item shown on screen
    private var lastPosition = -1

    override fun getItemCount(): Int {
        return youtubeList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoutubeLikeViewHolder {
        val binding: RowYoutubeLikeItemBinding =
            RowYoutubeLikeItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return YoutubeLikeViewHolder(context, binding, listener)
    }


    override fun onBindViewHolder(holder: YoutubeLikeViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val itemYoutubeVideo = youtubeList[position]

        holder.bind(itemYoutubeVideo)

        /*holder.viewBinding.cardViewItem.setOnClickListener {
            listener.onYoutubeItemClicked(
                holder.getImageView(),
                holder.getNameTextView(),
                holder.getDescriptionView(),
                itemYoutubeVideo,
                holder.absoluteAdapterPosition
            )
        }*/

        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left)
            holder.viewBinding.cardViewItem.startAnimation(animation)
            lastPosition = position
        }
    }
}