package com.riders.thelab.ui.mainactivity.fragment.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class NewsViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    @BindView(R.id.row_news_card_view)
    public CardView cardView;

    @BindView(R.id.row_news_image_view)
    ImageView ivNews;
    @BindView(R.id.row_news_text_view)
    TextView tvNews;

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
