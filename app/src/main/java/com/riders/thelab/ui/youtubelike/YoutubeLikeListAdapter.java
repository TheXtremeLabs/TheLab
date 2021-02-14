package com.riders.thelab.ui.youtubelike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Video;

import java.util.List;


/**
 * Created by michael on 14/04/2016.
 */
public class YoutubeLikeListAdapter extends RecyclerView.Adapter<YoutubeLikeViewHolder> {

    private final Context context;
    private final List<Video> youtubeList;
    private final YoutubeListClickListener listener;

    // Allows to remember the last item shown on screen
    private int lastPosition = -1;


    public YoutubeLikeListAdapter(Context context, List<Video> youtubeList,
                                  YoutubeListClickListener listener) {

        this.context = context;
        this.youtubeList = youtubeList;

        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        if (null != youtubeList) {
            return youtubeList.size();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public YoutubeLikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new YoutubeLikeViewHolder(
                context,
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.row_youtube_like_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final YoutubeLikeViewHolder holder, int position) {

        final Video itemYoutubeVideo = youtubeList.get(position);

        holder.setName(itemYoutubeVideo.getName());
        holder.setDescription(itemYoutubeVideo.getDescription());
        holder.setImage(itemYoutubeVideo.getImageThumb());

        holder.itemCardView.setOnClickListener(v ->
                listener.onYoutubeItemClicked(
                        holder.getImageView(),
                        itemYoutubeVideo,
                        holder.getAdapterPosition()));

        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation =
                    AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            holder.itemCardView.startAnimation(animation);
            lastPosition = position;
        }
    }
}