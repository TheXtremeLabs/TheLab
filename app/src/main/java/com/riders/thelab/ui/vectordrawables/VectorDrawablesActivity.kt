package com.riders.thelab.ui.vectordrawables

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivityVectorDrawablesBinding

class VectorDrawablesActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityVectorDrawablesBinding

    var googleIOAnimatedVectorDrawable: AnimatedVectorDrawable? = null
    var progressBarAnimatedVectorDrawable: AnimatedVectorDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityVectorDrawablesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.title = getString(R.string.activity_title_vector_drawables)
    }

    override fun onResume() {
        super.onResume()

        val drawableGoogleIO16: Drawable = viewBinding.ivAvdGoogleIo16.drawable
        val drawableProgressBar: Drawable = viewBinding.ivAvdProgressBar.drawable

        if (drawableGoogleIO16 is AnimatedVectorDrawable) {
            // AVD GoogleIO 16'
            googleIOAnimatedVectorDrawable = drawableGoogleIO16
            googleIOAnimatedVectorDrawable!!.start()
        }
        if (drawableProgressBar is AnimatedVectorDrawable) {
            // AVD Progress Bar
            progressBarAnimatedVectorDrawable = drawableProgressBar
            progressBarAnimatedVectorDrawable!!.start()
        }
    }
}
