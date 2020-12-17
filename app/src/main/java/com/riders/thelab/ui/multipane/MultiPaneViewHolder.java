package com.riders.thelab.ui.multipane;


import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

@SuppressLint("NonConstantResourceId")
public class MultiPaneViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.cv_multi_pane_item)
    public CardView cardView;
    @BindView(R.id.multi_pane_movie_item_title)
    TextView titleTextView;
    @BindView(R.id.multi_pane_movie_item_genre)
    TextView genreTextView;
    @BindView(R.id.multi_pane_movie_item_year)
    TextView yearTextView;

    public MultiPaneViewHolder(@NonNull View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull Movie movie) {
        titleTextView.setText(movie.getTitle());
        genreTextView.setText(movie.getGenre());
        yearTextView.setText(movie.getYear());
    }
}
