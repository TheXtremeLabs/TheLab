package com.riders.thelab.ui.mainactivity.fragment.news

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.riders.thelab.R
import com.riders.thelab.data.local.model.app.App
import com.riders.thelab.databinding.FragmentNewsBinding
import com.riders.thelab.ui.base.BaseFragment
import com.riders.thelab.ui.mainactivity.MainActivityAppClickListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NewsFragment : BaseFragment(), View.OnClickListener {

    companion object {

        private const val EXTRA_APP = "EXTRA_APP"

        fun newInstance(app: App): NewsFragment {
            val args = Bundle()
            args.putParcelable(EXTRA_APP, app)
            val fragment = NewsFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private var _viewBinding: FragmentNewsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mNewsViewModel: NewsViewModel by viewModels()

    private var item: App? = null

    /**
     * passing data between fragments
     */
    private var listener: MainActivityAppClickListener? = null


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val extras = arguments

        if (extras == null) {
            Timber.e("bundle is null - check the data you are trying to pass through please !")
        } else {
            Timber.e("get the data one by one")
            if (null != extras.getParcelable(EXTRA_APP)) {
                item = extras.getParcelable(EXTRA_APP)
            }
            /*(extras.getParcelable(EXTRA_APP) as App?)?.let {
                item = it
            }*/
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(rootView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(rootView, savedInstanceState)
        Timber.d("onViewCreated()")

        binding.app = item

        item?.let {
            Glide.with(requireContext())
                .load(it.appDrawableIcon)
                .placeholder(R.mipmap.ic_launcher_round)
                .apply {
                    dontTransform()
                }
                .into(binding.ivApp)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.e("onDestroyView()")

        _viewBinding = null
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.root_container -> item?.let { listener!!.onAppItemClickListener(view, it) }
            }

        }
    }

    override fun onConnected(isConnected: Boolean) {
        // Ignored
    }

}