package com.riders.thelab.ui.customtoast

import android.animation.Animator
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.riders.thelab.R
import com.riders.thelab.core.views.toast.TheLabToast
import com.riders.thelab.core.views.toast.ToastTypeEnum
import com.riders.thelab.databinding.ActivityCustomToastBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

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

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = getString(R.string.activity_title_custom_toast)

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
                        Completable.complete()
                            .delay(2, TimeUnit.SECONDS)
                            .doOnComplete {
                                runOnUiThread {
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
                            .doOnError {
                                Timber.e(it)
                            }
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe()
                    }

                    override fun onAnimationCancel(animation: Animator) {}
                    override fun onAnimationRepeat(animation: Animator) {}
                })
                .start()
            displayCustomToastUsingClass()
        }
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