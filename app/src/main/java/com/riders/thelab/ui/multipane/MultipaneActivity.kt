package com.riders.thelab.ui.multipane

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.riders.thelab.R
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.databinding.ActivityMultiPaneBinding
import com.riders.thelab.navigator.Navigator
import com.riders.thelab.ui.base.BaseActivity
import com.riders.thelab.ui.multipane.fragments.MultiPaneDetailFragment
import com.riders.thelab.ui.multipane.fragments.MultiPaneMainFragment
import com.riders.thelab.ui.splashscreen.SplashScreenActivity
import com.riders.thelab.ui.splashscreen.SplashScreenContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class MultipaneActivity : ComponentActivity(), MovieClickListener {

    private lateinit var viewBinding: ActivityMultiPaneBinding

    private val mMultiPaneViewModel: MultiPaneViewModel by viewModels()

    private lateinit var mAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMultiPaneBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            Timber.d("coroutine launch with name ${this.coroutineContext}")
            repeatOnLifecycle(Lifecycle.State.CREATED) {

                setContent {
                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MultiPaneContainer()
                        }
                    }
                }
            }
        }

        /*if (LabCompatibilityManager.isTablet(this)) {
            bindTabletViews()
        }*/

        initViewModelObservers()
    }

    override fun onResume() {
        super.onResume()

        Timber.d("onResume()")
        if (!LabCompatibilityManager.isTablet(this)) {
            mMultiPaneViewModel.fetchMovies()
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


    private fun initViewModelObservers() {

        mMultiPaneViewModel.getMovies().observe(this) {

            Timber.d("onMovieFetchedSuccess()")

            val mLayoutManager = LinearLayoutManager(this)
            viewBinding.rvMultiPane?.layoutManager = mLayoutManager
            viewBinding.rvMultiPane?.itemAnimator = DefaultItemAnimator()
            viewBinding.rvMultiPane?.addItemDecoration(
                DividerItemDecoration(
                    this,
                    LinearLayoutManager.VERTICAL
                )
            )

            mAdapter = MoviesAdapter(this, it, this)
            viewBinding.rvMultiPane?.adapter = mAdapter
        }
    }


    /*private fun bindTabletViews() {
        Timber.d("bindTabletViews()")
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fc_main_multi_pane,
                MultiPaneMainFragment.newInstance()
            )
            .commit()
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.fc_detail_movie_pane,
                MultiPaneDetailFragment.newInstance()
            )
            .commit()
    }*/

    /*override fun onMovieItemSelected(movie: Movie) {
        MultiPaneDetailFragment.newInstance(movie)
        if (null != fragment) {
            val args = Bundle()
            args.putParcelable(
                com.riders.thelab.ui.multipane.MultiPaneDetailFragment.BUNDLE_MOVIE,
                Parcels.wrap<Any>(movie)
            )
            fragment.arguments = args
            fragment.displayContent()
        } else {
            Timber.e("Something went wrong while loading data into fragment")
        }
    }*/

    override fun onMovieClicked(movie: Movie) {
        if (LabCompatibilityManager.isTablet(this))
            MultiPaneDetailFragment.newInstance(movie)
        else mMultiPaneViewModel.getMovieDetail(this, Navigator(this), movie)
    }
}