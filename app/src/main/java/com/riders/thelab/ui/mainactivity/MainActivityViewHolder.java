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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }


                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (0 != app.getIcon() && app.getTitle().equals("Palette")) {
                            Bitmap myBitmap = ((BitmapDrawable) resource).getBitmap();

                            Bitmap newBitmap = addGradient(myBitmap);
                            iconImageView.setImageDrawable(new BitmapDrawable(context.getResources(), newBitmap));
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


    }


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
