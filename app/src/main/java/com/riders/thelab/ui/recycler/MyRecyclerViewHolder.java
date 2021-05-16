package com.riders.thelab.ui.recycler;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.remote.dto.artist.Artist;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import timber.log.Timber;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;


@SuppressLint("NonConstantResourceId")
public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    private Context context;

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
    @BindView(R.id.iv_background_blurred)
    ShapeableImageView ivBackgroundBlurred;
    @BindView(R.id.transition_imageView)
    ShapeableImageView transitionImageView;
    @BindView(R.id.row_name_text_view)
    MaterialTextView nameTextView;

    public MyRecyclerViewHolder(@NonNull Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;

        ButterKnife.bind(this, itemView);
    }

    public void bind(Artist artist) {
        Timber.d(artist.toString());

        if (!LabCompatibilityManager.isTablet(context)) {
            ivBackgroundBlurred.setVisibility(View.VISIBLE);

            //Load the background  thumb image
            Glide.with(context)
                    .load(artist.getUrlThumb())
                    .apply(bitmapTransform(new BlurTransformation(5, 3)))
                    .into(ivBackgroundBlurred);
        }

        //Load the front image
        Glide.with(context)
                .load(artist.getUrlThumb())
                .into(transitionImageView);

        nameTextView.setText(artist.getArtistName());
    }

    public void storeItem(final Artist item, int position) {
        Timber.d("storeItem()");
        itemSelection = item;
        MyRecyclerViewHolder.position = position;
    }
}
