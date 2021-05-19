package com.riders.thelab.ui.youtubelike

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import com.riders.thelab.R
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.model.Video
import com.riders.thelab.databinding.ActivityYoutubeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class YoutubeLikeActivity : AppCompatActivity(), YoutubeListClickListener {

    private lateinit var viewBinding: ActivityYoutubeBinding

    @Inject
    lateinit var mYoutubeViewModel: YoutubeLikeViewModel

    private var contentAdapter: YoutubeLikeListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityYoutubeBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setupToolbar()

        mYoutubeViewModel.getProgressBarVisibility().observe(this, {
            if (!it) viewBinding.youtubeContentLoader.visibility = View.GONE
            else viewBinding.youtubeContentLoader.visibility = View.VISIBLE
        })

        mYoutubeViewModel.getConnectionStatus().observe(this, {
            viewBinding.noConnectionLinearContainer.visibility = View.VISIBLE
        })

        mYoutubeViewModel.getYoutubeVideosFailed().observe(this, {
            UIManager.showActionInToast(this, "Unable to fetch content")
        })

        mYoutubeViewModel.getYoutubeVideos().observe(this, { youtubeList ->
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            viewBinding.youtubeContentRecyclerView.layoutManager = layoutManager
            viewBinding.youtubeContentRecyclerView.itemAnimator = DefaultItemAnimator()

            if (!viewBinding.youtubeContentRecyclerView.isInLayout) {
                contentAdapter = YoutubeLikeListAdapter(this, youtubeList, this)
                viewBinding.youtubeContentRecyclerView.setVisibility(View.VISIBLE)
                viewBinding.youtubeContentRecyclerView.setAdapter(contentAdapter)
            }
        })

    }

    override fun onResume() {
        super.onResume()
    }

    fun setupToolbar() {

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(getString(R.string.activity_title_youtube_like))

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


    override fun onYoutubeItemClicked(
        thumbShapeableImageView: ShapeableImageView,
        titleTextView: MaterialTextView,
        descriptionTextView: MaterialTextView,
        video: Video,
        position: Int
    ) {
        mYoutubeViewModel.onYoutubeItemClicked(
            this,
            thumbShapeableImageView,
            titleTextView,
            descriptionTextView,
            video,
            position
        )
    }
}