package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.riders.thelab.R;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.utils.Validator;

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

    public void bindData(App app) {

        Glide.with(context)
                .load(
                        (0 != app.getIcon())
                                ? app.getIcon()
                                : app.getDrawableIcon())
                .into(iconImageView);

        titleTextView.setText(!Validator.isEmpty(app.getTitle())
                ? app.getTitle()
                : app.getName());

        descriptionTextView.setText(
                !Validator.isEmpty(app.getVersion())
                        ? app.getVersion()
                        : app.getDescription());
    }
}
