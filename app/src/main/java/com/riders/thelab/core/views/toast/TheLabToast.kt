package com.riders.thelab.core.views.toast

import android.app.Activity
import android.content.Context
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.riders.thelab.databinding.CustomToastLayoutBinding
import timber.log.Timber

/**
 * Reference : https://developer.android.com/guide/topics/ui/notifiers/toasts#CustomToastView
 */
class TheLabToast(
    private val context: Context
) : Toast(context) {
    private var _viewBinding: CustomToastLayoutBinding? = null
    private val binding get() = _viewBinding!!


    /**
     * Construct an empty Toast object.  You must call [.setView] before you
     * can call [.show].
     *
     * @param context The context to use.  Usually your [Application]
     * or [Activity] object.
     */
    init {

        run {
            _viewBinding = CustomToastLayoutBinding.inflate((context as Activity).layoutInflater)

            // Ref : https://developer.android.com/reference/android/widget/Toast#setGravity(int,%20int,%20int)
            setGravity(Gravity.BOTTOM, 0, 250)
            this.duration = LENGTH_LONG

            @Suppress("DEPRECATION")
            this.view = binding.root
        }
    }

    override fun setText(s: CharSequence) {
        binding.text.text = s
    }

    fun setType(toastTypeEnum: ToastTypeEnum?) {
        Timber.e("type : %s", toastTypeEnum.toString())
        setImageResource(toastTypeEnum!!.drawable)
        setBackgroundColor(toastTypeEnum.color)
    }

    fun setImageResource(drawableResourceID: Int) {
        binding.ivLol.setImageDrawable(ContextCompat.getDrawable(context, drawableResourceID))
    }

    /**
     * Reference : https://stackoverflow.com/questions/47837460/how-to-set-layout-background-tint-from-string-programmatically
     *
     * @param color
     */
    fun setBackgroundColor(color: Int) {
        binding.customToastContainer
            .background
            .setColorFilter(
                ContextCompat.getColor(context, color),
                PorterDuff.Mode.SRC_IN
            )
    }

    override fun show() {
        Timber.d("show()")
        val translateAnimation = TranslateAnimation(
            0f, 0f, 180f, binding.customToastContainer.top
                .toFloat()
        )

        translateAnimation.duration = 800
        translateAnimation.interpolator = LinearOutSlowInInterpolator()
        translateAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                Timber.d("onAnimationStart()")
            }

            override fun onAnimationEnd(animation: Animation) {
                Timber.e("onAnimationEnd()")
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(translateAnimation)
        binding.customToastContainer.startAnimation(animationSet)
        super.show()
    }

    override fun cancel() {
        Timber.e("cancel()")
        super.cancel()
        _viewBinding = null
    }

    class Builder(var context: Context) {
        var text: String? = null
        var type: ToastTypeEnum? = null
        fun setText(text: String?): Builder {
            this.text = text
            return this
        }

        fun setType(type: ToastTypeEnum?): Builder {
            this.type = type
            return this
        }

        fun show() {
            val toast = TheLabToast(context)
            toast.setText(text!!)
            toast.setType(type)
            toast.show()
        }
    }

}