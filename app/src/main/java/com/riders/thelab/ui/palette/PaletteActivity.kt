package com.riders.thelab.ui.palette


import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.databinding.ActivityPaletteBinding
import com.riders.thelab.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class PaletteActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityPaletteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPaletteBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        Glide.with(this)
            .load(Constants.PALETTE_URL)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e("onLoadingImageFailed()")
                    Timber.e(e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.i("Image is correctly downloaded")
                    try {
                        resource?.let {
                            val mBitmap: Bitmap = UIManager.drawableToBitmap(it)

                            //demande à la palette de générer ses coleurs, de façon asynchrone
                            //afin de ne pas bloquer l'interface graphique
                            //lorsque la palette est générée, je l'utilise sur mes textViews
                            Palette.Builder(mBitmap)
                                .generate { palette: Palette? ->
                                    if (palette != null) {
                                        appliquerPalette(palette)
                                    }
                                }

                        }
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                    return false
                }
            })
            .into(viewBinding.paletteImage)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


    fun appliquerPalette(palette: Palette) {
        run {

            //je récupère le swatch Vibrant
            val vibrant = palette.vibrantSwatch
            if (vibrant != null) { //il se peut que la palette ne génère pas tous les swatch

                //j'utilise getRgb() en tant que couleurs de fond te ma textView
                viewBinding.textVibrant.setBackgroundColor(vibrant.rgb)

                //getBodyTextColor() est prévu pour être affiché dessus une vue en background getRgb()
                viewBinding.textVibrant.setTextColor(vibrant.bodyTextColor)
            }
        }
        run {
            val vibrantDark = palette.darkVibrantSwatch
            if (vibrantDark != null) {
                viewBinding.textVibrantDark.setBackgroundColor(vibrantDark.rgb)
                viewBinding.textVibrantDark.setTextColor(vibrantDark.bodyTextColor)
            }
        }
        run {
            val vibrantLight = palette.lightVibrantSwatch
            if (vibrantLight != null) {
                viewBinding.textVibrantLight.setBackgroundColor(vibrantLight.rgb)
                viewBinding.textVibrantLight.setTextColor(vibrantLight.bodyTextColor)
            }
        }
        run {
            val muted = palette.mutedSwatch
            if (muted != null) {
                viewBinding.textMuted.setBackgroundColor(muted.rgb)
                viewBinding.textMuted.setTextColor(muted.bodyTextColor)
            }
        }
        run {
            val mutedDark = palette.darkMutedSwatch
            if (mutedDark != null) {
                viewBinding.textMutedDark.setBackgroundColor(mutedDark.rgb)
                viewBinding.textMutedDark.setTextColor(mutedDark.bodyTextColor)
            }
        }
        run {
            val lightMuted = palette.lightMutedSwatch
            if (lightMuted != null) {
                viewBinding.textMutedLight.setBackgroundColor(lightMuted.rgb)
                viewBinding.textMutedLight.setTextColor(lightMuted.bodyTextColor)
            }
        }
    }
}