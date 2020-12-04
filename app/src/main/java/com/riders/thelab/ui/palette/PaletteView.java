package com.riders.thelab.ui.palette;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.palette.graphics.Palette;

import com.riders.thelab.R;
import com.riders.thelab.ui.base.BaseViewImpl;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class PaletteView extends BaseViewImpl<PalettePresenter>
        implements PaletteContract.View {

    // TAG & Context
    private PaletteActivity context;

    private static String IMAGE_URL = "http://i.ytimg.com/vi/aNHOfJCphwk/maxresdefault.jpg";

    //Views
    @BindView(R.id.palette_image)
    ImageView imageView;

    @BindView(R.id.textVibrant)
    TextView textVibrant;
    @BindView(R.id.textVibrantLight)
    TextView textVibrantLight;
    @BindView(R.id.textVibrantDark)
    TextView textVibrantDark;
    @BindView(R.id.textMuted)
    TextView textMuted;
    @BindView(R.id.textMutedLight)
    TextView textMutedLight;
    @BindView(R.id.textMutedDark)
    TextView textMutedDark;


    @Inject
    PaletteView(PaletteActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        getPresenter().attachView(this);

        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.title_activity_palette));

        // InitViews
        ButterKnife.bind(this, context.findViewById(android.R.id.content));
        Timber.i("View initialized");

        //getImage();

        getPresenter().loadImage(imageView, IMAGE_URL);
    }

    public void getImage() {
        //j'utilise picasso afin de récupérer l'image
        Picasso.get()
                .load(IMAGE_URL)
                //.load(R.drawable.image1)
                .fit()
                .centerCrop()
                .into(imageView,
                        //j'écoute le chargement via picasso
                        new Callback() {
                            @Override

                            //puis lorsque l'image a bien été chargée
                            public void onSuccess() {

                                Timber.i("Image is correctly downloaded");

                                //retrouver le bitmap téléchargé par Picasso
                                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

                                //demande à la palette de générer ses coleurs, de façon asynchrone
                                //afin de ne pas bloquer l'interface graphique
                                new Palette.Builder(bitmap).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {

                                        //lorsque la palette est générée, je l'utilise sur mes textViews
                                        appliquerPalette(palette);
                                    }
                                });
                            }

                            @Override
                            public void onError(Exception e) {
                                Timber.e(e);
                                e.printStackTrace();
                            }
                        });
    }

    @Override
    public void onLoadingImageSuccessful(Bitmap bitmap) {
        Timber.i("Image is correctly downloaded");

        //retrouver le bitmap téléchargé par Picasso
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        //demande à la palette de générer ses coleurs, de façon asynchrone
        //afin de ne pas bloquer l'interface graphique
        //lorsque la palette est générée, je l'utilise sur mes textViews
        new Palette.Builder(bitmap).generate(this::appliquerPalette);
    }

    @Override
    public void onLoadingImageFailed() {
        Timber.e("onLoadingImageFailed()");
    }


    public void appliquerPalette(Palette palette) {

        {
            //je récupère le swatch Vibrant

            Palette.Swatch vibrant = palette.getVibrantSwatch();
            if (vibrant != null) { //il se peut que la palette ne génère pas tous les swatch

                //j'utilise getRgb() en tant que couleurs de fond te ma textView
                textVibrant.setBackgroundColor(vibrant.getRgb());

                //getBodyTextColor() est prévu pour être affiché dessus une vue en background getRgb()
                textVibrant.setTextColor(vibrant.getBodyTextColor());
            }
        }
        {
            Palette.Swatch vibrantDark = palette.getDarkVibrantSwatch();
            if (vibrantDark != null) {
                textVibrantDark.setBackgroundColor(vibrantDark.getRgb());
                textVibrantDark.setTextColor(vibrantDark.getBodyTextColor());
            }
        }
        {
            Palette.Swatch vibrantLight = palette.getLightVibrantSwatch();
            if (vibrantLight != null) {
                textVibrantLight.setBackgroundColor(vibrantLight.getRgb());
                textVibrantLight.setTextColor(vibrantLight.getBodyTextColor());
            }
        }

        {
            Palette.Swatch muted = palette.getMutedSwatch();
            if (muted != null) {
                textMuted.setBackgroundColor(muted.getRgb());
                textMuted.setTextColor(muted.getBodyTextColor());
            }
        }
        {
            Palette.Swatch mutedDark = palette.getDarkMutedSwatch();
            if (mutedDark != null) {
                textMutedDark.setBackgroundColor(mutedDark.getRgb());
                textMutedDark.setTextColor(mutedDark.getBodyTextColor());
            }
        }
        {
            Palette.Swatch lightMuted = palette.getLightMutedSwatch();
            if (lightMuted != null) {
                textMutedLight.setBackgroundColor(lightMuted.getRgb());
                textMutedLight.setTextColor(lightMuted.getBodyTextColor());
            }
        }
    }

    @Override
    public void onDestroy() {
        context = null;
    }
}
