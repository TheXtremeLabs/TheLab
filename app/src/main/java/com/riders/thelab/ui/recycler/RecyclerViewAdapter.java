package com.riders.thelab.ui.recycler;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import com.google.android.material.imageview.ShapeableImageView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.RecyclerItem;

import java.util.List;

import timber.log.Timber;


public class RecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    private RecyclerView recyclerView = null;

    private List<RecyclerItem> recyclerItemArrayList;
    private final RecyclerClickListener listener;

    private int mExpandedPosition = -1;

    private static ShapeableImageView transitionImageView;

    public RecyclerViewAdapter(List<RecyclerItem> recyclerItemArrayList,
                               RecyclerClickListener listener) {
        this.recyclerItemArrayList = recyclerItemArrayList;
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

        if (null != recyclerItemArrayList)
            return recyclerItemArrayList.size();
        return 0;
    }

    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRecyclerViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.row_recycler_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyRecyclerViewHolder holder, final int position) {

        RecyclerItem item = recyclerItemArrayList.get(position);

        holder.bind(item);

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

            listener.onRecyclerClick(item);

            // if the clicked item is already expanded then return -1
            //else return the position (this works with notifyDataSetChanged )
            mExpandedPosition = isExpanded ? -1 : position;

            //This will call the onBindViewHolder for all the itemViews on Screen
            notifyDataSetChanged();

            holder.storeItem(item, position);

            transitionImageView = holder.transitionImageView;
        });

        holder.btnDetail.setOnClickListener(detailView -> {
            Timber.e("setOnClickListener detailView ");
            listener.onDetailClick(item, transitionImageView, position);
        });

        holder.btnDelete.setOnClickListener(deleteView -> {
            Timber.e("setOnClickListener deleteView ");
            listener.onDeleteClick(item, position);
        });
    }

    public void removeItem(int position) {
        recyclerItemArrayList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(RecyclerItem item, int position) {
        recyclerItemArrayList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }
}
