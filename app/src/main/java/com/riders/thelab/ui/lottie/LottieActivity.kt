package com.riders.thelab.ui.lottie

import android.animation.Animator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import com.riders.thelab.databinding.ActivityLottieBinding
import timber.log.Timber
import kotlin.random.Random

class LottieActivity : AppCompatActivity() {

    private var _viewBinding: ActivityLottieBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private var isConnected: Boolean = false

    companion object {
        val lottieUrls = mutableListOf(
            // Android
            "https://assets1.lottiefiles.com/datafiles/8rza4J0CdJeJ8Pb3v9INOKI0vcekEpVccKVF2lNQ/android.json",
            // Search
            "https://assets4.lottiefiles.com/packages/lf20_83et0zjc.json",
            // Loading
            "https://assets1.lottiefiles.com/private_files/lf30_bn5winlb.json",
            "https://assets5.lottiefiles.com/private_files/lf30_qsg7wqkv.json",
            // Fluid loader
            "https://assets2.lottiefiles.com/packages/lf20_kk62um5v.json",
            // Swinging
            "https://assets6.lottiefiles.com/packages/lf20_97iupqly.json",
            // Scanning files
            "https://assets5.lottiefiles.com/private_files/lf30_jasmilgh.json",

            // Backgrounds
            // IT
            "https://assets10.lottiefiles.com/packages/lf20_k5dcqzxm.json",
            // Abstract
            "https://assets7.lottiefiles.com/packages/lf20_ym8w5cx4.json",
        )
    }

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewBinding = ActivityLottieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        postponeEnterTransition()
        binding.root.doOnPreDraw { startPostponedEnterTransition() }

        binding.lottieAnimationView.enableMergePathsForKitKatAndAbove(true)
        binding.lottieAnimationView.setOutlineMasksAndMattes(true)

        binding.lottieAnimationView.setAnimationFromUrl(lottieUrls[6])
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
    }


    override fun onDestroy() {
        Timber.e("onDestroy()")
        _viewBinding = null
        super.onDestroy()
    }

    private fun startRandomPlay() {

        binding.lottieAnimationView.setAnimationFromUrl(lottieUrls[3])
        binding.lottieAnimationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                Timber.d("onAnimationStart()")
            }

            override fun onAnimationEnd(animation: Animator?) {
                Timber.e("onAnimationEnd()")
            }

            override fun onAnimationCancel(animation: Animator?) {
                Timber.e("onAnimationCancel()")
            }

            override fun onAnimationRepeat(animation: Animator?) {
                Timber.d("onAnimationRepeat()")
                binding.lottieAnimationView.setAnimationFromUrl(
                    lottieUrls[Random.nextInt(
                        0,
                        lottieUrls.size
                    )]
                )
                binding.lottieAnimationView.playAnimation()
            }

        })
    }
}