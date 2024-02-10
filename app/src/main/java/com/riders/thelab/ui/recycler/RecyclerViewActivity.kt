package com.riders.thelab.ui.recycler

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.data.remote.dto.artist.Artist
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

@AndroidEntryPoint
class RecyclerViewActivity : BaseComponentActivity() {

    private val mRecyclerViewModel: RecyclerViewModel by viewModels()

    private var bucketUrl: String? = null
    private var artistThumbnails: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModelObservers()

        mRecyclerViewModel.getFirebaseJSONURL(this)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            RecyclerViewContent(viewModel = mRecyclerViewModel)
                        }
                    }
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun backPressed() {
        Timber.e("onBackPressed()")
    }

    private fun initViewModelObservers() {
        mRecyclerViewModel.getJSONURLFetched().observe(this) {
            bucketUrl = it
            mRecyclerViewModel.getFirebaseFiles(this)
        }
        mRecyclerViewModel.getJSONURLError().observe(this) {
            Timber.e("getJSONURLError().observe | $it")
        }
        mRecyclerViewModel.getArtistsThumbnailsSuccessful().observe(this) {
            this.artistThumbnails = it
            bucketUrl?.let { url -> mRecyclerViewModel.fetchArtists(url) }
        }
        mRecyclerViewModel.getArtistsThumbnailsError().observe(this) {
            Timber.e("getArtistsThumbnailsError().observe | $it")

        }

        mRecyclerViewModel.getArtistsError().observe(this) {
            Timber.e("getArtistsError().observe | $it")

        }
    }

    fun onDetailClick(artist: Artist) {
        Intent(this, RecyclerViewDetailActivity::class.java)
            .apply {
                this.putExtra(
                    RecyclerViewDetailActivity.EXTRA_RECYCLER_ITEM,
                    Json.encodeToString(artist)
                )
            }.run {
                startActivity(this)
            }
    }
}