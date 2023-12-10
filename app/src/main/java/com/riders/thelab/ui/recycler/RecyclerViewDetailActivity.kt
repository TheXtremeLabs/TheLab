package com.riders.thelab.ui.recycler

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import com.riders.thelab.R
import com.riders.thelab.core.common.utils.LabCompatibilityManager
import com.riders.thelab.core.data.remote.dto.artist.Artist
import com.riders.thelab.core.ui.utils.UIManager
import com.riders.thelab.databinding.ActivityRecyclerViewDetailBinding
import kotlinx.serialization.json.Json
import timber.log.Timber

class RecyclerViewDetailActivity : AppCompatActivity(), OnOffsetChangedListener {

    companion object {
        const val EXTRA_RECYCLER_ITEM = "recycler_item"
        const val EXTRA_TRANSITION_ICON_NAME = "icon"
        var isShow = false
    }

    private var _viewDetailBinding: ActivityRecyclerViewDetailBinding? = null
    private val binding get() = _viewDetailBinding!!

    private var item: Artist? = null

    private var scrollRange = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewDetailBinding = ActivityRecyclerViewDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundle()
        initCollapsingToolbar()
        bindData()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewDetailBinding = null
    }


    /**
     * Retrieve bundle passed from Recycler View Activity
     *
     * And bind value in artist object
     */
    @Suppress("DEPRECATION")
    @SuppressLint("NewApi")
    private fun getBundle() {
        Timber.d("getBundle()")

        // Try to get bundle values
        intent.extras?.let {
            val extraRecyclerItemJson: String? = if (!LabCompatibilityManager.isTiramisu()) {
                it.getSerializable(EXTRA_RECYCLER_ITEM) as String?
            } else {
                it.getSerializable(EXTRA_RECYCLER_ITEM, String::class.java)
            }

            extraRecyclerItemJson?.let { recyclerItem ->
                item = Json.decodeFromString(recyclerItem)
                // Log
                Timber.d("item : %s", item.toString())
            } ?: run { Timber.e("Extra recycler view item object is null") }
        } ?: run { Timber.e("Intent extras are null") }
    }

    private fun initCollapsingToolbar() {
        setSupportActionBar(binding.toolbarRecyclerViewDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //binding.collapsingToolbarRecyclerViewDetail.title = item.artistName
        binding.collapsingToolbarRecyclerViewDetail
            .setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent))
        binding.appBarLayout.setExpanded(true)
        binding.appBarLayout.addOnOffsetChangedListener(this)
    }

    /**
     * Display artist value
     */
    private fun bindData() {
        item?.let { artist: Artist ->

            binding.artist = artist

            if (LabCompatibilityManager.isTablet(this)) {

                //Load the background  thumb image
                binding.ivBackgroundBlurred?.let {
                    UIManager.loadImageBlurred(
                        this,
                        artist.urlThumb,
                        it
                    )
                }
                //viewDetailBinding.tvDebutesDetail?.text = String.format("Since : %s", item.debutes)
            }

            // Display image
            UIManager.loadImage(this, artist.urlThumb, binding.transitionImageView)
        }

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


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAfterTransition()
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (scrollRange == -1) {
            scrollRange = appBarLayout!!.totalScrollRange
        }
        if (scrollRange + verticalOffset == 0) {
            //binding.collapsingToolbarRecyclerViewDetail.title = item.artistName
            isShow = true
        } else if (isShow) {
            binding.collapsingToolbarRecyclerViewDetail.title = " "
            isShow = false
        }
    }
}