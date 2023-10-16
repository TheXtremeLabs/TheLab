package com.riders.thelab.ui.youtubelike

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.Video
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.databinding.ActivityYoutubeBinding
import com.riders.thelab.navigator.Navigator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YoutubeLikeActivity : AppCompatActivity(), YoutubeListClickListener {

    private lateinit var viewBinding: ActivityYoutubeBinding

    private val mYoutubeViewModel: YoutubeLikeViewModel by viewModels()

    private var contentAdapter: YoutubeLikeListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityYoutubeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setupToolbar()
        initViewModelsObservers()

        mYoutubeViewModel.fetchVideos(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    private fun setupToolbar() {
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.swipeDownColorPrimary
                )
            )
        )
        window.statusBarColor = ContextCompat.getColor(
            this, R.color.swipeDownColorPrimaryDark
        )
    }

    private fun initViewModelsObservers() {

        mYoutubeViewModel.getProgressBarVisibility().observe(this) {
            if (!it) viewBinding.youtubeContentLoader.visibility = View.GONE
            else viewBinding.youtubeContentLoader.visibility = View.VISIBLE
        }

        mYoutubeViewModel.getConnectionStatus().observe(this) {
            viewBinding.noConnectionLinearContainer.visibility = View.VISIBLE
        }

        mYoutubeViewModel.getYoutubeVideosFailed().observe(this) {
            UIManager.showActionInToast(this, "Unable to fetch content")
        }

        mYoutubeViewModel.getYoutubeVideos().observe(this) { youtubeList ->
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            viewBinding.youtubeContentRecyclerView.layoutManager = layoutManager
            viewBinding.youtubeContentRecyclerView.itemAnimator = DefaultItemAnimator()

            if (!viewBinding.youtubeContentRecyclerView.isInLayout) {
                contentAdapter = YoutubeLikeListAdapter(this, youtubeList, this)
                viewBinding.youtubeContentRecyclerView.visibility = View.VISIBLE
                viewBinding.youtubeContentRecyclerView.adapter = contentAdapter
            }
        }

    }

    override fun onYoutubeItemClicked(view: View, video: Video) {

        val iv = view.findViewById<ShapeableImageView>(R.id.image_item)
        val tv = view.findViewById<MaterialTextView>(R.id.name_item)
        val desc = view.findViewById<MaterialTextView>(R.id.description_item)

        mYoutubeViewModel.onYoutubeItemClicked(
            this,
            Navigator(this),
            iv,
            tv,
            desc,
            video
        )
    }


    override fun onYoutubeItemClicked(
        thumbShapeableImageView: ShapeableImageView,
        titleTextView: MaterialTextView,
        descriptionTextView: MaterialTextView,
        video: Video,
        position: Int
    ) {
        mYoutubeViewModel.onYoutubeItemClicked(
            this,
            Navigator(this),
            thumbShapeableImageView,
            titleTextView,
            descriptionTextView,
            video,
            position
        )
    }
}