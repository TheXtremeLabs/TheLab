package com.riders.thelab.ui.recycler

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.databinding.ActivityRecyclerViewDetailBinding
import timber.log.Timber
import java.util.*

class RecyclerViewDetailActivity : AppCompatActivity(), OnOffsetChangedListener {

    companion object {
        const val EXTRA_RECYCLER_ITEM = "recycler_item"
        const val EXTRA_TRANSITION_ICON_NAME = "icon"
        var isShow = false
    }

    private lateinit var viewDetailBinding: ActivityRecyclerViewDetailBinding

    private lateinit var item: Artist

    var scrollRange = -1

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
        item = extras!!.getParcelable(EXTRA_RECYCLER_ITEM)!!
        Timber.d("item : %s", item.toString())
    }

    private fun initCollapsingToolbar() {

        setSupportActionBar(viewDetailBinding.toolbarRecyclerViewDetail)
        Objects.requireNonNull(supportActionBar)?.setDisplayHomeAsUpEnabled(true)

        viewDetailBinding.collapsingToolbarRecyclerViewDetail.title = item.artistName
        viewDetailBinding.collapsingToolbarRecyclerViewDetail
            .setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent))
        viewDetailBinding.appBarLayout.setExpanded(true)
        viewDetailBinding.appBarLayout.addOnOffsetChangedListener(this)
    }

    /**
     * Display artist value
     */
    private fun bindData() {
        viewDetailBinding.artist = item

        if (LabCompatibilityManager.isTablet(this)) {
            //Load the background  thumb image
            viewDetailBinding.ivBackgroundBlurred?.let {
                UIManager.loadImageBlurred(
                    this,
                    item.urlThumb,
                    it
                )
            }
            //viewDetailBinding.tvDebutesDetail?.text = String.format("Since : %s", item.debutes)
        }

        // Display image
        UIManager.loadImage(this, item.urlThumb, viewDetailBinding.transitionImageView)


        /*val sb = StringBuilder()
        sb.append(item.firstName)
        if (item.secondName.isNotBlank()) sb.append(", ").append(item.secondName)
        sb.append(" ").append(item.lastName)

        viewDetailBinding.tvFullNameDetail.text = sb.toString()*/
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (scrollRange == -1) {
            scrollRange = appBarLayout!!.totalScrollRange
        }
        if (scrollRange + verticalOffset == 0) {
            viewDetailBinding.collapsingToolbarRecyclerViewDetail.title = item.artistName
            isShow = true
        } else if (isShow) {
            viewDetailBinding.collapsingToolbarRecyclerViewDetail.title = " "
            isShow = false
        }
    }
}