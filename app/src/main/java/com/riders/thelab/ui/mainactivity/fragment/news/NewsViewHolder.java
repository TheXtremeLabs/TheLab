package com.riders.thelab.ui.mainactivity.fragment.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class NewsViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    @BindView(R.id.row_news_card_view)
    public MaterialCardView cardView;

    @BindView(R.id.row_news_image_view)
    ShapeableImageView ivNews;
    @BindView(R.id.row_news_text_view)
    MaterialTextView tvNews;

    public NewsViewHolder(Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;
        ButterKnife.bind(this, itemView);
    }

    public void bind(App app) {
        Timber.d("bind()");
        Glide.with(context)
                .load(app.getIcon())
                .into(ivNews);

        tvNews.setText(app.getTitle());
    }
}
