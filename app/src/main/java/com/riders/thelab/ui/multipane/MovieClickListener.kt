package com.riders.thelab.ui.multipane

import com.riders.thelab.data.local.model.Movie

interface MovieClickListener {
    fun onMovieClicked(movie: Movie)
}