package com.riders.thelab.ui.multipane;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Movie;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MultiPaneViewHolder> {

    private final Context context;
    private final List<Movie> moviesList;
    private final MovieClickListener listener;

    public MoviesAdapter(Context context, List<Movie> moviesList, MovieClickListener listener) {
        this.context = context;
        this.moviesList = moviesList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        if (moviesList != null) {
            return moviesList.size();
        }
        return 0;
    }

    @Override
    public @NotNull MultiPaneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MultiPaneViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.row_multi_pane,
                        parent,
                        false
                ));
    }

    @Override
    public void onBindViewHolder(MultiPaneViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        holder.bind(movie);

        holder.cardView.setOnClickListener(v -> listener.onMovieClicked(movie));
    }
}
