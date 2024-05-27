package com.riders.thelab.ui.vectordrawables

import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import com.riders.thelab.core.ui.compose.base.BaseAppCompatActivity
import com.riders.thelab.databinding.ActivityVectorDrawablesBinding

class VectorDrawablesActivity : BaseAppCompatActivity() {

    lateinit var viewBinding: ActivityVectorDrawablesBinding

    private var googleIOAnimatedVectorDrawable: AnimatedVectorDrawable? = null
    private var progressBarAnimatedVectorDrawable: AnimatedVectorDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityVectorDrawablesBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                backPressed()
            }
        }
        return true
    }

    override fun backPressed() {
        finish()
    }
}
