package com.riders.thelab.ui.multipane

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.databinding.ActivityMultiPaneDetailBinding
import org.parceler.Parcels
import timber.log.Timber
import java.util.*

class MultipaneDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_MOVIE = "MOVIE"
    }

    private lateinit var viewBinding: ActivityMultiPaneDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMultiPaneDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        getBundle()
    }

    private fun getBundle() {
        val bundle = intent.extras
        if (null == bundle) {
            Timber.e("Bundle is null exit activity.")
            finish()
        }
        val movie: Movie = Parcels.unwrap(
            Objects.requireNonNull(bundle)!!.getParcelable(
                EXTRA_MOVIE
            )
        )
        supportActionBar?.title = movie.title
        setViews(movie)
    }

    private fun setViews(movie: Movie) {
        Glide.with(this)
            .load(movie.urlThumbnail)
            .into(viewBinding.multiPaneMovieItemImageDetail)
        viewBinding.multiPaneMovieItemTitleDetail.text = movie.title
        viewBinding.multiPaneMovieItemGenreDetail.text = movie.genre
        viewBinding.multiPaneMovieItemYearDetail.text = movie.year
    }
}