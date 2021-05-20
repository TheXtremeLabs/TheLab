package com.riders.thelab.ui.spring

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.DynamicAnimation.ViewProperty
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import com.riders.thelab.databinding.ActivitySpringBinding

class SpringActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivitySpringBinding

    private var xAnimation: SpringAnimation? = null
    private var yAnimation: SpringAnimation? = null

    private var dX = 0f
    private var dY = 0f


    private val globalLayoutListener = OnGlobalLayoutListener {
        xAnimation = createSpringAnimation(
            viewBinding.imageView, DynamicAnimation.X, viewBinding.imageView.getX(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )
        yAnimation = createSpringAnimation(
            viewBinding.imageView, DynamicAnimation.Y, viewBinding.imageView.getY(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )
    }


    @SuppressLint("ClickableViewAccessibility")
    private val touchListener = OnTouchListener { v: View, event: MotionEvent ->
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // capture the difference between view's top left corner and touch point
                dX = v.x - event.rawX
                dY = v.y - event.rawY
                // cancel animations
                xAnimation!!.cancel()
                yAnimation!!.cancel()
            }
            MotionEvent.ACTION_MOVE -> {
                //  a different approach would be to change the view's LayoutParams.
                viewBinding.imageView.animate()
                    .x(event.rawX + dX)
                    .y(event.rawY + dY)
                    .setDuration(0)
                    .start()
                val newX = event.rawX + dX
                val newY = event.rawY + dY
                v
                    .animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start()
            }
            MotionEvent.ACTION_UP -> {
                xAnimation!!.start()
                yAnimation!!.start()
            }
        }
        true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivitySpringBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.imageView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
        chainedSpringAnimation()
    }

    fun createSpringAnimation(
        view: View?,
        property: ViewProperty?,
        finalPosition: Float,
        stiffness: Float,
        dampingRatio: Float
    ): SpringAnimation {
        val animation = SpringAnimation(view, property)
        val springForce = SpringForce(finalPosition)
        springForce.stiffness = stiffness
        springForce.dampingRatio = dampingRatio
        animation.spring = springForce
        return animation
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun chainedSpringAnimation() {
        val xAnimation2 = createSpringAnimation(
            viewBinding.imageView2, DynamicAnimation.X, viewBinding.imageView2.getX(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )
        val yAnimation2 = createSpringAnimation(
            viewBinding.imageView2, DynamicAnimation.Y, viewBinding.imageView2.getY(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )
        val xAnimation3 = createSpringAnimation(
            viewBinding.imageView3, DynamicAnimation.X, viewBinding.imageView3.getX(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )
        val yAnimation3 = createSpringAnimation(
            viewBinding.imageView3, DynamicAnimation.Y, viewBinding.imageView3.getY(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )
        val xAnimation4 = createSpringAnimation(
            viewBinding.imageView4, DynamicAnimation.X, viewBinding.imageView4.getX(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )
        val yAnimation4 = createSpringAnimation(
            viewBinding.imageView4, DynamicAnimation.Y, viewBinding.imageView4.getY(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )
        val xAnimation5 = createSpringAnimation(
            viewBinding.imageView5, DynamicAnimation.X, viewBinding.imageView5.getX(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )
        val yAnimation5 = createSpringAnimation(
            viewBinding.imageView5, DynamicAnimation.Y, viewBinding.imageView5.getY(),
            SpringForce.STIFFNESS_MEDIUM, SpringForce.DAMPING_RATIO_HIGH_BOUNCY
        )

        val imageView2Params = viewBinding.imageView2.getLayoutParams() as MarginLayoutParams
        val imageView3Params = viewBinding.imageView3.getLayoutParams() as MarginLayoutParams
        val imageView4Params = viewBinding.imageView4.getLayoutParams() as MarginLayoutParams
        val imageView5Params = viewBinding.imageView5.getLayoutParams() as MarginLayoutParams
        xAnimation2.addUpdateListener { dynamicAnimation: DynamicAnimation<*>?, v: Float, v1: Float ->
            xAnimation3.animateToFinalPosition(
                v + (viewBinding.imageView2.width - viewBinding.imageView3.width) / 2
            )
        }
        yAnimation2.addUpdateListener { dynamicAnimation: DynamicAnimation<*>?, v: Float, v1: Float ->
            yAnimation3.animateToFinalPosition(
                v + viewBinding.imageView2.height + imageView3Params.topMargin
            )
        }
        xAnimation3.addUpdateListener { dynamicAnimation: DynamicAnimation<*>?, v: Float, v1: Float ->
            xAnimation4.animateToFinalPosition(
                v + (viewBinding.imageView3.width - viewBinding.imageView4.width) / 2
            )
        }
        yAnimation3.addUpdateListener { dynamicAnimation: DynamicAnimation<*>?, v: Float, v1: Float ->
            yAnimation4.animateToFinalPosition(
                v + viewBinding.imageView3.height + imageView4Params.topMargin
            )
        }
        xAnimation4.addUpdateListener { dynamicAnimation: DynamicAnimation<*>?, v: Float, v1: Float ->
            xAnimation5.animateToFinalPosition(
                v + (viewBinding.imageView4.width - viewBinding.imageView5.width) / 2
            )
        }
        yAnimation4.addUpdateListener { dynamicAnimation: DynamicAnimation<*>?, v: Float, v1: Float ->
            yAnimation5.animateToFinalPosition(
                v + viewBinding.imageView4.height + imageView5Params.topMargin
            )
        }

        viewBinding.imageView.setOnTouchListener { view: View, motionEvent: MotionEvent ->
            when (motionEvent.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    dX = view.x - motionEvent.rawX
                    dY = view.y - motionEvent.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    val newX = motionEvent.rawX + dX
                    val newY = motionEvent.rawY + dY
                    view
                        .animate()
                        .x(newX)
                        .y(newY)
                        .setDuration(0)
                        .start()
                    xAnimation2.animateToFinalPosition(
                        newX + (viewBinding.imageView.width - viewBinding.imageView2.width) / 2
                    )
                    yAnimation2.animateToFinalPosition(
                        newY + viewBinding.imageView2.height + imageView2Params.topMargin
                    )
                }
            }
            true
        }
    }
}