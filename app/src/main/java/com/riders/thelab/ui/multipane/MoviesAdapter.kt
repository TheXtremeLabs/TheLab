package com.riders.thelab.ui.multipane

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.R
import com.riders.thelab.data.local.model.Movie

class MoviesAdapter(
    private val context: Context,
    private val moviesList: List<Movie>,
    private val listener: MovieClickListener
) : RecyclerView.Adapter<MultiPaneViewHolder>() {

    override fun getItemCount(): Int {
        return moviesList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiPaneViewHolder {
        return MultiPaneViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.row_multi_pane,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(holder: MultiPaneViewHolder, position: Int) {
        val movie: Movie = moviesList[position]
        holder.bind(movie)
        holder.viewBinding.cvMultiPaneItem.setOnClickListener { listener.onMovieClicked(movie) }
    }
}