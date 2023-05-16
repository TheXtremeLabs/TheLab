package com.riders.thelab.ui.customtoast

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.riders.thelab.R
import com.riders.thelab.core.views.toast.TheLabToast
import com.riders.thelab.core.views.toast.ToastTypeEnum
import com.riders.thelab.databinding.ActivityCustomToastBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Random

@AndroidEntryPoint
class CustomToastActivity : AppCompatActivity() {
    private var context: Context? = null

    lateinit var viewBinding: ActivityCustomToastBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        val w = window
        w.statusBarColor = Color.TRANSPARENT

        viewBinding = ActivityCustomToastBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        context = this

        viewBinding.buttonCustom.setOnClickListener {
            viewBinding.progressIndicator.animate()
                .setDuration(2000)
                .alpha(1.0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        viewBinding.progressIndicator.visibility = View.VISIBLE
                        viewBinding.progressIndicator.alpha = 0f
                        viewBinding.progressIndicator.setIndicatorColor(
                            ContextCompat.getColor(context!!, R.color.success),
                            ContextCompat.getColor(context!!, R.color.warning),
                            ContextCompat.getColor(context!!, R.color.error)
                        )
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(2000)
                            viewBinding.progressIndicator.animate()
                                .setDuration(1500)
                                .alpha(0.0f)
                                .setListener(object : Animator.AnimatorListener {
                                    override fun onAnimationStart(animation: Animator) {}
                                    override fun onAnimationEnd(animation: Animator) {
                                        viewBinding.progressIndicator.visibility = View.GONE
                                    }

                                    override fun onAnimationCancel(animation: Animator) {}
                                    override fun onAnimationRepeat(animation: Animator) {}
                                })
                        }
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                .start()
            displayCustomToastUsingClass()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(false)
    }

    private fun displayCustomToastUsingClass() {
        val random = Random().nextInt(ToastTypeEnum.values().size)

        Timber.d("random : %s", random)

        TheLabToast.Builder(this)
            .setText("Testing a new text")
            .setType(ToastTypeEnum.values()[random])
            .show()
    }
}