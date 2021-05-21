package com.riders.thelab.ui.youtubelike


import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.palette.graphics.Palette
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.databinding.ActivityYoutubeDetailBinding
import jp.wasabeef.glide.transformations.BlurTransformation
import timber.log.Timber
import java.util.*

class YoutubeLikeDetailActivity : AppCompatActivity() {

    companion object {
        //Bundle Arguments
        const val VIDEO_OBJECT_ARG = "content_video"
    }

    private lateinit var viewBinding: ActivityYoutubeDetailBinding

    private lateinit var item: Video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val w = window
        w.allowEnterTransitionOverlap = true

        /*
        Where myBitmap is the Image from which you want to extract the color.
        Also for API 21 and above, you'll need to add the following flags if you're planning to color the status bar and navigation bar:
         */
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        viewBinding = ActivityYoutubeDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Tell the framework to wait.
        supportPostponeEnterTransition()

        getBundle()

        loadContent()
    }

    private fun getBundle() {
        val extras = intent.extras
        if (extras == null) {
            Timber.e("bundle is null - check the data you are trying to pass through please !")
        } else {
            Timber.e("get the data one by one")
            item = extras.getParcelable(VIDEO_OBJECT_ARG)!!
        }
    }

    private fun loadContent() {
        Objects.requireNonNull(supportActionBar)!!.title = item.name

        //Load the background  thumb image
        Glide.with(this)
            .load(item.imageUrl)
            .apply(RequestOptions.bitmapTransform(BlurTransformation(5, 3)))
            .into(viewBinding.contentImageThumbBlurred)


        //Load the thumb image clicked before
        Glide.with(this)
            .load(item.imageUrl)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?, model: Any,
                    target: Target<Drawable>, isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable, model: Any,
                    target: Target<Drawable>, dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()

                    //retrouver le bitmap téléchargé par Picasso
                    val bitmap = (resource as BitmapDrawable).bitmap

                    //demande à la palette de générer ses coleurs, de façon asynchrone
                    //afin de ne pas bloquer l'interface graphique
                    Palette.Builder(bitmap).generate { palette: Palette? ->
                        assert(palette != null)
                        generatePalette(palette)
                    }
                    return false
                }
            })
            .into(viewBinding.contentImageThumb)
        viewBinding.contentTextName.text = item.name
        viewBinding.contentTextDescription.text = item.description
    }


    /**
     * Generate palette in order to change toolbar's color
     *
     * @param palette
     */
    fun generatePalette(palette: Palette?) {
        run {
            val muted = palette!!.mutedSwatch

            //il se peut que la palette ne génère pas tous les swatch
            if (muted != null) {
                //j'utilise getRgb() en tant que couleur de fond de ma toolbar
                Objects.requireNonNull(supportActionBar)
                    ?.setBackgroundDrawable(ColorDrawable(muted.rgb))
            }
        }
        run {
            val mutedDark = palette!!.darkMutedSwatch
            if (mutedDark != null) {
                window.statusBarColor = mutedDark.rgb
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}