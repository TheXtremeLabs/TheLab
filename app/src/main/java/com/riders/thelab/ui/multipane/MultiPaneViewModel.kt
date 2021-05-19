package com.riders.thelab.ui.multipane

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.data.RepositoryImpl
import com.riders.thelab.data.local.bean.MovieEnum
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.navigator.Navigator
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.parceler.Parcels
import javax.inject.Inject

class MultiPaneViewModel @Inject constructor(
    val repositoryImpl: RepositoryImpl,
    var navigator: Navigator
) : ViewModel() {

    private var movies: MutableLiveData<List<Movie>> = MutableLiveData()

    val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun getMovies(): LiveData<List<Movie>> {
        return movies
    }

    fun fetchMovies() {
        val list: MutableList<Movie> = MovieEnum.getMovies() as MutableList<Movie>
        movies.value = list
    }

    fun getMovieDetail(activity: Context, movie: Movie) {
        val intent = Intent(activity, MultipaneDetailActivity::class.java)
        intent.putExtra(MultipaneDetailActivity.EXTRA_MOVIE, Parcels.wrap<Any>(movie))
        navigator.callMultipaneDetailActivity(intent)
    }
}