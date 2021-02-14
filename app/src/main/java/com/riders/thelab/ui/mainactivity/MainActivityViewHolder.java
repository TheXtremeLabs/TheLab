package com.riders.thelab.ui.mainactivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

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
import com.riders.thelab.data.local.model.App;
import com.riders.thelab.utils.Validator;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint({"UnknownNullness", "NonConstantResourceId"})
public class MainActivityViewHolder extends RecyclerView.ViewHolder {

    private final Context context;

    @BindView(R.id.row_item_cardView)
    public MaterialCardView itemCardView;
    @BindView(R.id.iv_row_item_background)
    ShapeableImageView backgroundImageView;
    @BindView(R.id.row_icon_imageView)
    ShapeableImageView iconImageView;
    @BindView(R.id.row_title_textView)
    MaterialTextView titleTextView;
    @BindView(R.id.row_description_textView)
    MaterialTextView descriptionTextView;
    @BindView(R.id.arrow_icon)
    ShapeableImageView ivArrow;


    public MainActivityViewHolder(@NonNull Context context, @NonNull View itemView) {
        super(itemView);

        this.context = context;

        ButterKnife.bind(this, itemView);
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

                            Bitmap newBitmap = addGradient(myBitmap);
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

        titleTextView.setText(!Validator.isEmpty(app.getTitle())
                ? app.getTitle()
                : app.getName());

        descriptionTextView.setText(
                !Validator.isEmpty(app.getVersion())
                        ? app.getVersion()
                        : app.getDescription());


        // Load background image
        Glide.with(context)
                .load(
                        null != app.getActivity()
                                ? app.getIcon()
                                : app.getDrawableIcon())
                .into(backgroundImageView);

    }


    /**
     * Reference : https://stackoverflow.com/questions/37775675/imageview-set-color-filter-to-gradient
     *
     * @param originalBitmap
     * @return
     */
    public Bitmap addGradient(Bitmap originalBitmap) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);

        canvas.drawBitmap(originalBitmap, 0, 0, null);

        Paint paint = new Paint();

        int[] colors = {
                ContextCompat.getColor(context, R.color.admin_splash_bg),
                ContextCompat.getColor(context, R.color.adminDashboardColorPrimary),
                ContextCompat.getColor(context, R.color.adminDashboardSelectedItemAccent),
                ContextCompat.getColor(context, R.color.multiPaneColorPrimaryDark),
        };

        LinearGradient shader =
                new LinearGradient(
                        0, 0,
                        0, height,
                        colors,
                        null,
                        Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, width, height, paint);

        return updatedBitmap;
    }
}
