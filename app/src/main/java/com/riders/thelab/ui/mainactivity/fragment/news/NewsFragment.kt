package com.riders.thelab.ui.mainactivity.fragment.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.FragmentNewsBinding
import com.riders.thelab.ui.mainactivity.MainActivityAppClickListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NewsFragment : Fragment(), View.OnClickListener {

    lateinit var viewBinding: FragmentNewsBinding

    private val mNewsViewModel: NewsViewModel by viewModels()

    private val recentAppsNames = arrayOf("Music", "Spring", "Weather")
    private var mRecentApps: List<App>? = null

    /**
     * passing data between fragments
     */
    private var listener: MainActivityAppClickListener? = null


    companion object {
        fun newInstance(): NewsFragment {
            val args = Bundle()

            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener =
            if (context is MainActivityAppClickListener) {
                context
            } else {
                throw ClassCastException(
                    context.toString()
                            + " must implement MainActivityAppClickListener"
                )
            }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentNewsBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)
        Timber.d("onViewCreated()")

        viewBinding.newsCardView1.setOnClickListener(this)
        viewBinding.newsCardView2.setOnClickListener(this)
        viewBinding.newsCardView3.setOnClickListener(this)

        mNewsViewModel
            .getRecentApps()
            .observe(
                requireActivity(),
                { recentApps ->
                    mRecentApps = recentApps
                    setupCards(recentApps)
                })

        mNewsViewModel.fetchRecentApps(requireContext(), recentAppsNames)
    }

    override fun onDestroyView() {
        Timber.d("onDestroyView()")
        super.onDestroyView()
    }


    private fun setupCards(appList: List<App>) {
        for (i in appList.indices) {
            if (i == 0) {
                bindData(viewBinding.newsImageView1, viewBinding.newsTextView1, appList[i])
            }
            if (i == 1) {
                bindData(viewBinding.newsImageView2, viewBinding.newsTextView2, appList[i])
            }
            if (i == 2) {
                bindData(viewBinding.newsImageView3, viewBinding.newsTextView3, appList[i])
            }
        }
    }

    private fun bindData(
        imageView: ShapeableImageView,
        textView: MaterialTextView,
        app: App
    ) {
        if (app.appDrawableIcon != null) {
            imageView.setImageDrawable(app.appDrawableIcon)
        }
        textView.text = if (app.appName != null) app.appName else app.appTitle
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.news_card_view_1 -> mRecentApps?.let {
                    listener!!.onAppItemCLickListener(
                        requireView(),
                        it[0],
                        0
                    )
                }
                R.id.news_card_view_2 -> mRecentApps?.let {
                    listener!!.onAppItemCLickListener(
                        requireView(),
                        it[1],
                        0
                    )
                }
                R.id.news_card_view_3 -> mRecentApps?.let {
                    listener!!.onAppItemCLickListener(
                        requireView(),
                        it[2],
                        0
                    )
                }
            }
        }
    }
}