package com.riders.thelab.ui.recycler;


import android.annotation.SuppressLint;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.RecyclerItem;
import com.riders.thelab.data.remote.dto.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


@SuppressLint("NonConstantResourceId")
public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    private static Artist itemSelection;
    private static int position;
    @BindView(R.id.row_card_view)
    public MaterialCardView cardView;
    @BindView(R.id.row_details_linear_layout)
    public RelativeLayout detailsLinearLayout;
    @BindView(R.id.row_detail_btn)
    public MaterialButton btnDetail;
    @BindView(R.id.row_delete_btn)
    public AppCompatImageButton btnDelete;
    @BindView(R.id.transition_imageView)
    ShapeableImageView transitionImageView;
    @BindView(R.id.row_name_text_view)
    MaterialTextView nameTextView;

    public MyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(Artist artist) {
        Timber.d(artist.toString());
        nameTextView.setText(artist.getArtistName());
    }

    public void storeItem(final Artist item, int position) {
        Timber.d("storeItem()");
        itemSelection = item;
        MyRecyclerViewHolder.position = position;
    }
}
