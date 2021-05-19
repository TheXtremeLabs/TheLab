package com.riders.thelab.ui.palette;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.palette.graphics.Palette;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.riders.thelab.R;
import com.riders.thelab.core.utils.UIManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class PaletteView extends BaseViewImpl<PalettePresenter>
        implements PaletteContract.View {
    // TAG & Context
    private PaletteActivity context;

    //Views
    @BindView(R.id.palette_image)
    ShapeableImageView imageView;
    @BindView(R.id.textVibrant)
    MaterialTextView textVibrant;
    @BindView(R.id.textVibrantLight)
    MaterialTextView textVibrantLight;
    @BindView(R.id.textVibrantDark)
    MaterialTextView textVibrantDark;
    @BindView(R.id.textMuted)
    MaterialTextView textMuted;
    @BindView(R.id.textMutedLight)
    MaterialTextView textMutedLight;
    @BindView(R.id.textMutedDark)
    MaterialTextView textMutedDark;

    @Inject
    PaletteView(PaletteActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        getPresenter().attachView(this);

        context.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context.getSupportActionBar().setTitle(context.getString(R.string.activity_title_palette));

        // InitViews
        ButterKnife.bind(this, context.findViewById(android.R.id.content));

        getPresenter().loadImage(imageView, Constants.PALETTE_URL);
    }

    @Override
    public void onLoadingImageSuccessful(Drawable drawable) {
        try {
            Bitmap mBitmap = UIManager.drawableToBitmap(drawable);

            //demande à la palette de générer ses coleurs, de façon asynchrone
            //afin de ne pas bloquer l'interface graphique
            //lorsque la palette est générée, je l'utilise sur mes textViews
            new Palette.Builder(mBitmap).generate(this::appliquerPalette);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
        getPresenter().detachView();
        context = null;
    }
}
