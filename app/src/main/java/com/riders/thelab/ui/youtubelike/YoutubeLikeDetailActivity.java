package com.riders.thelab.ui.youtubelike;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.LabCompatibilityManager;
import com.riders.thelab.data.local.model.Video;
import com.riders.thelab.ui.base.SimpleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;
import timber.log.Timber;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

@SuppressLint({"NonConstantResourceId", "NewApi"})
public class YoutubeLikeDetailActivity extends SimpleActivity {

    private Context mContext;
    //Bundle Arguments
    public static final String VIDEO_OBJECT_ARG = "content_video";

    @BindView(R.id.content_image_thumb)
    ShapeableImageView imageThumb;
    @BindView(R.id.content_image_thumb_blurred)
    ShapeableImageView imageThumbBlurred;
    @BindView(R.id.content_text_name)
    MaterialTextView titleTextView;
    @BindView(R.id.content_text_description)
    MaterialTextView descriptionTextView;

    private Video item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window w = getWindow();
        w.setAllowEnterTransitionOverlap(true);

        /*
        Where myBitmap is the Image from which you want to extract the color.
        Also for API 21 and above, you'll need to add the following flags if you're planning to color the status bar and navigation bar:
         */
        if (LabCompatibilityManager.isLollipop()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        setContentView(R.layout.activity_youtube_detail);

        supportPostponeEnterTransition();
        mContext = this;

        ButterKnife.bind(this);

        getBundle();

        if (null != item) {
            loadContent();
        } else {
            Timber.e("Cannot load content");
        }
    }

    private void getBundle() {
        Bundle extras = getIntent().getExtras();

        if (extras == null) {
            Timber.e("bundle is null - check the data you are trying to pass through please !");
        } else {
            Timber.e("get the data one by one");

            item = extras.getParcelable(VIDEO_OBJECT_ARG);
        }
    }

    private void loadContent() {

        getSupportActionBar().setTitle(item.getName());

        //Load the background  thumb image
        Glide.with(this)
                .load(item.getImageThumb())
                .apply(bitmapTransform(new BlurTransformation(5, 3)))
                .into(imageThumbBlurred);


        //Load the thumb image clicked before
        Glide.with(this)
                .load(item.getImageThumb())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        supportStartPostponedEnterTransition();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {

                        supportStartPostponedEnterTransition();

                        //retrouver le bitmap téléchargé par Picasso
                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();

                        //demande à la palette de générer ses coleurs, de façon asynchrone
                        //afin de ne pas bloquer l'interface graphique
                        new Palette.Builder(bitmap).generate(palette -> {

                            //lorsque la palette est générée, je l'utilise sur mes textViews
                            generatePalette(palette);
                        });
                        return false;
                    }
                })
                .into(imageThumb);

        titleTextView.setText(item.getName());
        descriptionTextView.setText(item.getDescription());

    }


    /**
     * Generate palette in order to change toolbar's color
     *
     * @param palette
     */
    public void generatePalette(Palette palette) {
        {
            Palette.Swatch muted = palette.getMutedSwatch();

            //il se peut que la palette ne génère pas tous les swatch
            if (muted != null) {
                //j'utilise getRgb() en tant que couleur de fond de ma toolbar
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(muted.getRgb()));
            }
        }
        {
            Palette.Swatch mutedDark = palette.getDarkMutedSwatch();
            if (mutedDark != null) {
                if (LabCompatibilityManager.isLollipop()) {
                    getWindow().setStatusBarColor(mutedDark.getRgb());
                }
            }

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAfterTransition();
    }
}
