package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.core.utils.UIManager;
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.utils.Validator;

@SuppressLint({"UnknownNullness", "NonConstantResourceId"})
public class MainActivityViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    public RelativeLayout itemRelativeLayout;
    LinearLayout backgroundLinearLayout;

    public MaterialCardView itemCardView;
    ShapeableImageView backgroundImageView;
    ShapeableImageView iconImageView;
    MaterialTextView titleTextView;
    MaterialTextView descriptionTextView;
    ShapeableImageView ivArrow;


    public MainActivityViewHolder(@NonNull Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;

        if (!LabCompatibilityManager.isTablet(context)) {
            bindSmartphoneViews(itemView);
        } else {
            bindTabletViews(itemView);
        }
    }

    private void bindSmartphoneViews(View itemView) {
        itemCardView = itemView.findViewById(R.id.row_item_cardView);
        backgroundImageView = itemView.findViewById(R.id.iv_row_item_background);
        iconImageView = itemView.findViewById(R.id.row_icon_imageView);
        titleTextView = itemView.findViewById(R.id.row_title_textView);
        descriptionTextView = itemView.findViewById(R.id.row_description_textView);
        ivArrow = itemView.findViewById(R.id.arrow_icon);
    }

    private void bindTabletViews(View itemView) {
        itemRelativeLayout = itemView.findViewById(R.id.card_frame_layout);
        backgroundLinearLayout = itemView.findViewById(R.id.ll_card_selected_background);
        itemCardView = itemView.findViewById(R.id.row_item_cardView);
        backgroundImageView = itemView.findViewById(R.id.iv_row_item_background);
        titleTextView = itemView.findViewById(R.id.row_title_textView);
        descriptionTextView = itemView.findViewById(R.id.row_description_textView);
    }

    public void bindData(App app) {

        // Load left icon drawable
        Glide.with(context)
                .load(
                        (0 != app.getIcon())
                                ? app.getIcon()
                                : app.getDrawableIcon())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }


                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {

                        if (0 != app.getIcon() && app.getTitle().equals("Palette")) {
                            Bitmap myBitmap = ((BitmapDrawable) resource).getBitmap();

                            Bitmap newBitmap = UIManager.addGradientToImageView(context, myBitmap);
                            iconImageView.setImageDrawable(
                                    new BitmapDrawable(context.getResources(), newBitmap));
                            return true;
                        }

                        if (0 != app.getIcon() && app.getTitle().equals("WIP")) {
                            iconImageView.setImageDrawable(
                                    ContextCompat.getDrawable(context, R.drawable.logo_testing));
                            ivArrow.setVisibility(View.GONE);
                            return true;
                        }

                        return false;
                    }
                })
                .into(iconImageView);

        bindTitleAndDescription(app);

        // Load background image
        Glide.with(context)
                .load(
                        null != app.getActivity()
                                ? app.getIcon()
                                : app.getDrawableIcon())
                .into(backgroundImageView);
    }

    public void bindTabletData(App app) {
        // Load background image
        Glide.with(context)
                .load(
                        null != app.getActivity()
                                ? app.getIcon()
                                : app.getDrawableIcon())
                .into(backgroundImageView);

        bindTitleAndDescription(app);
    }

    public void bindTitleAndDescription(App app) {
        titleTextView.setText(!Validator.isEmpty(app.getTitle())
                ? app.getTitle()
                : app.getName());

        descriptionTextView.setText(
                !Validator.isEmpty(app.getVersion())
                        ? app.getVersion()
                        : app.getDescription());

    }
}
