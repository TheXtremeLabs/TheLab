package com.riders.thelab.ui.recycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.databinding.ActivityRecyclerViewDetailBinding
import org.parceler.Parcels
import timber.log.Timber
import java.util.*

class RecyclerViewDetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_RECYCLER_ITEM = "recycler_item"
        const val EXTRA_TRANSITION_ICON_NAME = "icon"
    }

    private lateinit var viewDetailBinding: ActivityRecyclerViewDetailBinding

    private lateinit var item: Artist


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDetailBinding = ActivityRecyclerViewDetailBinding.inflate(layoutInflater)
        setContentView(viewDetailBinding.root)

        getBundle()
        initCollapsingToolbar()
        bindData()
    }


    /**
     * Retrieve bundle passed from Recycler View Activity
     *
     * And bind value in artist object
     */
    private fun getBundle() {
        val extras = intent.extras
        item = Parcels.unwrap(extras!!.getParcelable(EXTRA_RECYCLER_ITEM))
        Timber.d("item : %s", item.toString())
    }

    private fun initCollapsingToolbar() {

        setSupportActionBar(viewDetailBinding.toolbarRecyclerViewDetail)
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)

        viewDetailBinding.collapsingToolbarRecyclerViewDetail.title = item.artistName
        viewDetailBinding.collapsingToolbarRecyclerViewDetail
            .setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent))
        viewDetailBinding.appBarLayout.setExpanded(true)
        viewDetailBinding.appBarLayout.addOnOffsetChangedListener(object : OnOffsetChangedListener {
            var isShow = false
            var scrollRange = -1
            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    viewDetailBinding.collapsingToolbarRecyclerViewDetail.title = item.artistName
                    isShow = true
                } else if (isShow) {
                    viewDetailBinding.collapsingToolbarRecyclerViewDetail.title = " "
                    isShow = false
                }
            }
        })
    }

    /**
     * Display artist value
     */
    private fun bindData() {
        if (LabCompatibilityManager.isTablet(this)) {
            //Load the background  thumb image
            viewDetailBinding.ivBackgroundBlurred?.let {
                UIManager.loadImageBlurred(
                    this,
                    item.urlThumb,
                    it
                )
            }
            viewDetailBinding.tvDebutesDetail?.text = String.format("Since : %s", item.debutes)
        }

        // Display image
        Glide.with(this)
            .load(item.urlThumb)
            .into(viewDetailBinding.transitionImageView)

        viewDetailBinding.tvNameDetail.text = item.artistName

        val sb = StringBuilder()
        sb.append(item.firstName)
        if (item.secondName.isNotBlank()) sb.append(", ").append(item.secondName)
        sb.append(" ").append(item.lastName)

        viewDetailBinding.tvFullNameDetail.text = sb.toString()
        viewDetailBinding.tvActivitiesDetail.text = item.activities
        viewDetailBinding.tvDescriptionDetail.text = item.description
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }
}