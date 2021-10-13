package com.riders.thelab.ui.screenshot

import android.graphics.Bitmap
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.riders.thelab.databinding.ActivityScreenShotBinding
import jp.wasabeef.glide.transformations.BlurTransformation
import timber.log.Timber
import java.io.File
import java.util.*


class ScreenShotActivity : AppCompatActivity() {

    private var _viewBinding: ActivityScreenShotBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private lateinit var outputDirectory: File


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityScreenShotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Timber.d("onCreate()")

        /*
        * A ViewTreeObserver gets fired for different drawing events.
        * Usually the OnGlobalLayoutListener is what you want for getting the measurement,
        * so the code in the listener will be called after the layout phase,
        * so the measurements are ready
        * */
        window.decorView.rootView
            .viewTreeObserver
            .addOnGlobalLayoutListener(
                object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        Timber.d("binding.root is ready")
                        window.decorView.rootView
                            .viewTreeObserver
                            .removeOnGlobalLayoutListener(this)
                    }
                })

    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }

    fun onTakeScreenShotClicked(view: View) {
        Timber.e("takeScreenShot()")

        val now = Date()
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        try {
            val screenView = window.decorView.rootView

            Timber.d("View : $screenView | width : ${screenView.width}, height : ${screenView.height} ")

            // create bitmap screen capture
            screenView.isDrawingCacheEnabled = true
            val bitmap: Bitmap = Bitmap.createBitmap(screenView.drawingCache)
            screenView.isDrawingCacheEnabled = false

            Glide
                .with(this@ScreenShotActivity)
                .load(bitmap)
                .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 3)))
                .into(binding.ivScreenShot)

        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}