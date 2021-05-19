package com.riders.thelab.ui.multipane.fragments

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.riders.thelab.R
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.databinding.FragmentMultiPaneDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import org.parceler.Parcels
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class MultiPaneDetailFragment : Fragment() {

    companion object {
        const val BUNDLE_MOVIE = "movie"

        fun newInstance(): MultiPaneDetailFragment {
            val args = Bundle()

            val fragment = MultiPaneDetailFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(movie: Movie): MultiPaneDetailFragment {
            val args = Bundle()
            args.putParcelable(
                BUNDLE_MOVIE,
                Parcels.wrap<Any>(movie)
            )
            val fragment = MultiPaneDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }

    lateinit var viewBinding: FragmentMultiPaneDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMultiPaneDetailBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayContent()
    }

    ///////////////////////////////////////////
    //
    //  Override methods
    //
    ///////////////////////////////////////////
    fun displayContent() {
        val bundle = this.arguments
        if (null == bundle) {
            Timber.e("Bundle is null - Cannot retrieve movie")
            return
        }
        val movie: Movie =
            Parcels.unwrap(bundle.getParcelable(BUNDLE_MOVIE))
        setTextOnFragment(movie)
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun setTextOnFragment(movie: Movie) {
        showLoader()

        Timber.e("URL RECEIVE : %s", movie.urlThumbnail)

        Glide.with(Objects.requireNonNull(activity)!!)
            .load(movie.urlThumbnail)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e("Picasso - OOOOOOOHHH CA VA PAAAAAS LAAAAA !!!")
                    hideLoader()
                    viewBinding.multiPaneMovieImageDetail.setImageResource(R.mipmap.ic_launcher)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    Timber.e("Picasso - Image bien téléchargée et affichée !!!")
                    hideLoader()
                    return false
                }
            })
            .into(viewBinding.multiPaneMovieImageDetail)

        viewBinding.multiPaneMovieTitleDetail.text = movie.title
        viewBinding.multiPaneMovieGenreDetail.text = movie.genre
        viewBinding.multiPaneMovieYearDetail.text = movie.year
    }


    private fun showLoader() {
        viewBinding.multiPaneMovieLoaderImage.visibility = View.VISIBLE
    }


    private fun hideLoader() {
        viewBinding.multiPaneMovieLoaderImage.visibility = View.GONE
    }

}