package com.riders.thelab.ui.recycler

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.imageview.ShapeableImageView
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.remote.dto.artist.Artist
import com.riders.thelab.databinding.ActivityRecyclerViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class RecyclerViewActivity : AppCompatActivity(), RecyclerClickListener {

    lateinit var viewBinding: ActivityRecyclerViewBinding

    private lateinit var adapter: RecyclerViewAdapter

    private val mRecyclerViewModel: RecyclerViewModel by viewModels()

    private var bucketUrl: String? = null
    private var artistThumbnails: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRecyclerViewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        initViewModelObservers()

        mRecyclerViewModel.getFirebaseJSONURL(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }


    fun initViewModelObservers() {
        mRecyclerViewModel.getJSONURLFetched().observe(this, {
            bucketUrl = it
            mRecyclerViewModel.getFirebaseFiles(this)
        })
        mRecyclerViewModel.getJSONURLError().observe(this, {

        })
        mRecyclerViewModel.getArtistsThumbnailsSuccessful().observe(this, {

            this.artistThumbnails = it
            bucketUrl?.let { url -> mRecyclerViewModel.fetchArtists(url) }
        })
        mRecyclerViewModel.getArtistsThumbnailsError().observe(this, {

        })
        mRecyclerViewModel.getArtists().observe(this, {

            CoroutineScope(Dispatchers.Main).launch {
                delay(3000)
                UIManager.hideView(viewBinding.progressBar)
                adapter = RecyclerViewAdapter(
                    this@RecyclerViewActivity,
                    it,
                    artistThumbnails!!,
                    this@RecyclerViewActivity
                )

                val layoutManager: LinearLayoutManager =
                    if (!LabCompatibilityManager.isTablet(this@RecyclerViewActivity))
                        LinearLayoutManager(
                            this@RecyclerViewActivity,
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                    else
                        GridLayoutManager(
                            this@RecyclerViewActivity,
                            3,
                            GridLayoutManager.VERTICAL,
                            false
                        )

                viewBinding.recyclerView.layoutManager = layoutManager
                viewBinding.recyclerView.itemAnimator = DefaultItemAnimator()
                viewBinding.recyclerView.adapter = adapter
            }
        })

        mRecyclerViewModel.getArtistsError().observe(this, {

        })
    }

    override fun onRecyclerClick(artist: Artist) {
        Timber.d("onRecyclerClick()")
        Timber.d(artist.toString())
    }

    override fun onDetailClick(artist: Artist, sharedImageView: ShapeableImageView, position: Int) {
        mRecyclerViewModel.onDetailClick(this, artist, sharedImageView)
    }

    override fun onDeleteClick(artist: Artist, position: Int) {
        mRecyclerViewModel.onDeleteClick(this, artist, adapter, position)
    }
}