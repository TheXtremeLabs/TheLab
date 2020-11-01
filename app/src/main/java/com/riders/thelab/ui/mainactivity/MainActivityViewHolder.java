package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riders.thelab.R;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint({"UnknownNullness", "NonConstantResourceId"})
public class MainActivityViewHolder extends RecyclerView.ViewHolder {

    private Context context;

    @BindView(R.id.row_item_cardView)
    public CardView itemCardView;

    @BindView(R.id.row_icon_imageView)
    AppCompatImageView iconImageView;

    @BindView(R.id.row_title_textView)
    TextView titleTextView;


    @BindView(R.id.row_description_textView)
    TextView descriptionTextView;


    public MainActivityViewHolder(@NonNull Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;

        ButterKnife.bind(this, itemView);
    }

    public void bindData(@NonNull String title, @NonNull String version, @NonNull Drawable icon) {

        Glide.with(context)
                .load(icon)
                .into(iconImageView);

        titleTextView.setText(title);
        descriptionTextView.setText(version);
    }
}
