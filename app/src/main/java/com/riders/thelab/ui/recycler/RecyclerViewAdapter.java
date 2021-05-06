package com.riders.thelab.ui.recycler;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.remote.dto.Artist;

import java.util.List;

import timber.log.Timber;


public class RecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    private Context mContext;
    private static ShapeableImageView transitionImageView;
    private final List<Artist> artistList;
    private List<String> artistThumbnails;
    private final RecyclerClickListener listener;
    private RecyclerView recyclerView = null;
    private int mExpandedPosition = -1;

    public RecyclerViewAdapter(@NonNull Context context,
                               List<Artist> artistList,
                               List<String> artistThumbnails,
                               RecyclerClickListener listener) {
        this.mContext = context;
        this.artistList = artistList;
        this.artistThumbnails = artistThumbnails;
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.recyclerView = recyclerView;

        // fancy animations can skip if like
        TransitionManager.beginDelayedTransition(recyclerView);
    }

    @Override
    public int getItemCount() {

        if (null != artistList)
            return artistList.size();
        return 0;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRecyclerViewHolder(
                mContext,
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.row_recycler_view, parent, false));
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull final MyRecyclerViewHolder holder, final int position) {

        Artist artist = artistList.get(position);

        if (!LabCompatibilityManager.isNougat()) {
            for (String element : artistThumbnails) {
                if (element.contains(artist.getUrlThumb())) {
                    artist.setUrlThumb(element);
                }
            }
        } else {
            artist
                    .setUrlThumb(
                            artistThumbnails
                                    .stream()
                                    .filter(element -> element.contains(artist.getUrlThumb()))
                                    .findFirst()
                                    .orElse(""));
        }

        holder.bind(artist);

        // This line checks if the item displayed on screen
        // was expanded or not (Remembering the fact that Recycler View )
        // reuses views so onBindViewHolder will be called for all
        // items visible on screen.
        final boolean isExpanded = position == mExpandedPosition;

        //This line hides or shows the layout in question
        holder.detailsLinearLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

        // I do not know what the heck this is :)
        holder.itemView.setActivated(isExpanded);

        // Click event for each item (itemView is an in-built variable of holder class)
        holder.cardView.setOnClickListener(v -> {

            listener.onRecyclerClick(artist);

            // if the clicked item is already expanded then return -1
            //else return the position (this works with notifyDataSetChanged )
            mExpandedPosition = isExpanded ? -1 : position;

            //This will call the onBindViewHolder for all the itemViews on Screen
            notifyDataSetChanged();

            holder.storeItem(artist, position);

            transitionImageView = holder.transitionImageView;
        });

        holder.btnDetail.setOnClickListener(detailView -> {
            Timber.e("setOnClickListener detailView ");
            listener.onDetailClick(artist, transitionImageView, position);
        });

        holder.btnDelete.setOnClickListener(deleteView -> {
            Timber.e("setOnClickListener deleteView ");
            listener.onDeleteClick(artist, position);
        });
    }

    public void removeItem(int position) {
        artistList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Artist item, int position) {
        artistList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
