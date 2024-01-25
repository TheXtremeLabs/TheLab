package com.riders.thelab.feature.theaters

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class TheatersDetailActivity : BaseComponentActivity() {

    private val mViewModel: TheatersDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getBundle(this@TheatersDetailActivity, this.intent)

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    val tmdbItem: TMDBItemModel by mViewModel.tmdbItemUiState.collectAsStateWithLifecycle()

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            TheatersDetailContent(
                                tmdbItem = tmdbItem,
                                isTrailerVisible = mViewModel.isTrailerVisible
                            ) {
                                mViewModel.updateIsTrailerVisible(it)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        Timber.e("backPressed()")

        if (mViewModel.isTrailerVisible) {
            mViewModel.updateIsTrailerVisible(false)
        } else {
            finish()
        }
    }

    companion object {
        const val EXTRA_MOVIE = "MOVIE"
        const val EXTRA_TMDB_ITEM = "MOVIE"
    }
}