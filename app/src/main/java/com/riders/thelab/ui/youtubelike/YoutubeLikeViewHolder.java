package com.riders.thelab.ui.youtubelike;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Video;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class YoutubeLikeViewHolder extends RecyclerView.ViewHolder {

    // TAG
    private final Context context;

    // Views
    @BindView(R.id.card_view_item)
    public MaterialCardView itemCardView;

    @BindView(R.id.loader_item)
    ProgressBar itemLoader;
    @BindView(R.id.image_item)
    ShapeableImageView imageThumb;
    @BindView(R.id.name_item)
    MaterialTextView nameTextView;
    @BindView(R.id.description_item)
    MaterialTextView descriptionTextView;


    public YoutubeLikeViewHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    public ShapeableImageView getImageView() {
        return this.imageThumb;
    }

    public void setImage(String imageURL) {
        Glide.with(context)
                .load(imageURL)
                .into(imageThumb);
    }

    public void setName(String name) {
        nameTextView.setText(name);
    }

    public void setDescription(String description) {
        descriptionTextView.setText(description);
    }

    public void bind(Video itemYoutubeVideo) {
        if (itemLoader != null) {
            itemLoader.setVisibility(View.VISIBLE);
        }
    }

}
