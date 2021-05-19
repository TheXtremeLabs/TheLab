package com.riders.thelab.ui.multipane

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.databinding.RowMultiPaneBinding

class MultiPaneViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val viewBinding: RowMultiPaneBinding = RowMultiPaneBinding.bind(itemView)

    fun bind(movie: Movie) {
        viewBinding.multiPaneMovieItemTitle.text = movie.title
        viewBinding.multiPaneMovieItemGenre.text = movie.genre
        viewBinding.multiPaneMovieItemYear.text = movie.year
    }
}