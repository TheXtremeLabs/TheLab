package com.riders.thelab.ui.mainactivity.fragment.home

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabGlideUtils
import com.riders.thelab.databinding.FragmentHomeBinding
import com.riders.thelab.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _viewBinding: FragmentHomeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mHomeViewModel: HomeViewModel by viewModels()

    private var isConnected: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timber.d("onCreateView()")

        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated()")

        postponeEnterTransition()

        if (!isConnected) {
            loadImage(null)
        } else {
            initViewModelsObservers()
            mHomeViewModel.getWallpaperImages(requireActivity())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.e("onDestroyView()")

        _viewBinding = null
    }

    private fun initViewModelsObservers() {
        Timber.d("initViewModelsObservers() ")

        mHomeViewModel
            .getImagesFetchedDone()
            .observe(
                requireActivity(),
                {
                    Timber.d("getImagesFetchedDone() ")
                })

        mHomeViewModel
            .getImagesFetchedFailed()
            .observe(
                requireActivity(),
                {
                    Timber.e("getImagesFetchedFailed() ")
                })

        mHomeViewModel
            .getImageUrl()
            .observe(
                requireActivity()
            ) { url ->
                // Display image
                loadImage(url)

            }
    }


    private fun loadImage(url: String?) {
        Timber.i("loadImage($url) ")
        LabGlideUtils.getInstance().loadImage(
            requireActivity(),
            url ?: R.drawable.logo_colors,
            binding.ivBackground,
            object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    startPostponedEnterTransition()
                    return false
                }
            })
    }

    override fun onConnected(isConnected: Boolean) {
        this.isConnected = isConnected
    }
}