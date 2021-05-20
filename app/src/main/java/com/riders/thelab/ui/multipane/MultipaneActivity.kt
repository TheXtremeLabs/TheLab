package com.riders.thelab.ui.multipane

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.riders.thelab.R
import com.riders.thelab.core.utils.LabCompatibilityManager
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.databinding.ActivityMultiPaneBinding
import com.riders.thelab.ui.multipane.fragments.MultiPaneDetailFragment
import com.riders.thelab.ui.multipane.fragments.MultiPaneMainFragment
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class MultipaneActivity : AppCompatActivity(), MovieClickListener {

    private lateinit var viewBinding: ActivityMultiPaneBinding

    private val mMultiPaneViewModel: MultiPaneViewModel by viewModels()

    private var mAdapter: MoviesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMultiPaneBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = getString(R.string.activity_title_multipane)

        if (LabCompatibilityManager.isTablet(this)) {
            bindTabletViews()
        }

        initViewModelObservers()

    }

    override fun onResume() {
        super.onResume()

        Timber.d("onResume()")
        if (!LabCompatibilityManager.isTablet(this)) {
            mMultiPaneViewModel.fetchMovies()
        }
    }

    private fun initViewModelObservers() {

        mMultiPaneViewModel.getMovies().observe(this, {

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
        })
    }


    private fun bindTabletViews() {
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
    }

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
        MultiPaneDetailFragment.newInstance(movie)
    }
}