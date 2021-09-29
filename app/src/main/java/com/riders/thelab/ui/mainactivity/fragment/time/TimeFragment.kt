package com.riders.thelab.ui.mainactivity.fragment.time

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.databinding.FragmentTimeBinding
import com.riders.thelab.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

@AndroidEntryPoint
class TimeFragment : BaseFragment() {
    lateinit var viewBinding: FragmentTimeBinding

    private val mTimeViewModel: TimeViewModel by viewModels()

    private var mThread: Thread? = null

    companion object {
        fun newInstance(): TimeFragment {
            val args = Bundle()

            val fragment = TimeFragment()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTimeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTimeViewModel.getProgressVisibility().observe(
            requireActivity(),
            {
                if (!it) UIManager.hideView(viewBinding.progressBar)
                else UIManager.showView(viewBinding.progressBar)
            })

        mTimeViewModel
            .getImagesFetchedDone()
            .observe(
                requireActivity(),
                {
                    Timber.d("getImagesFetchedDone() ")
                })

        mTimeViewModel
            .getImagesFetchedFailed()
            .observe(
                requireActivity(),
                {
                    Timber.e("getImagesFetchedFailed() ")
                })

        mTimeViewModel
            .getImageUrl()
            .observe(
                requireActivity(),
                { url ->

                    // Display image
                    Glide.with(requireActivity())
                        .load(url)
                        .into(viewBinding.ivTimeBackground)

                })

        mTimeViewModel.getWallpaperImages(requireActivity())
    }

    override fun onPause() {
        if (null != mThread && !mThread!!.isInterrupted) {
            mThread!!.interrupt()
            mThread = null
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        mThread = object : Thread() {
            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(1000)
                        requireActivity().runOnUiThread {
                            val localTime = LocalTime.now()
                            viewBinding.tvTime.text =
                                localTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                        }
                    }
                } catch (e: InterruptedException) {
                    Timber.e(e)
                }
            }
        }
        (mThread as Thread).start()

        val localDate = LocalDate.now()
        viewBinding.tvDate.text = localDate.format(DateTimeFormatter.ofPattern("EEE, d MMM yyyy"))

        if (!LabCompatibilityManager.isTablet(requireActivity()))
            mTimeViewModel.getWallpaperImages(requireActivity())
    }

    override fun onDestroyView() {
        Timber.d("onDestroyView()")

        if (null != mThread) {
            mThread!!.interrupt()
            mThread = null
        }

        super.onDestroyView()
    }

    override fun onConnected(isConnected: Boolean) {

    }
}