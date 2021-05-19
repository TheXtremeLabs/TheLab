package com.riders.thelab.ui.multipane;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.riders.thelab.R;
import com.riders.thelab.data.local.bean.MovieEnum;
import com.riders.thelab.data.local.model.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

@SuppressLint("NonConstantResourceId")
public class MultiPaneMainFragment extends Fragment
        implements MovieClickListener {

    private Context mContext = null;

    @BindView(R.id.rv_multi_pane_main)
    RecyclerView recyclerView;

    private Unbinder unbinder;

    private List<Movie> movieList;
    private MoviesAdapter mAdapter;

    /**
     * passing data between fragments
     */
    private OnItemSelectedListener listener;

    public interface OnItemSelectedListener {
        void onMovieItemSelected(Movie movie);
    }


    public static MultiPaneMainFragment newInstance() {
        Bundle args = new Bundle();

        MultiPaneMainFragment fragment = new MultiPaneMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    ///////////////////////////////////////////
    //
    //  Override methods
    //
    ///////////////////////////////////////////
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MultiPaneActivity.OnItemSelectedListener");
        }
    }

    /**
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_multi_pane_main, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchData();
        setUpRecyclerView();
        buildAndApplyAdapter();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    ///////////////////////////////////////////
    //
    //  Class methods
    //
    ///////////////////////////////////////////
    private void fetchData() {
        Timber.d("fetchData");
        movieList = MovieEnum.getMovies();
    }

    private void setUpRecyclerView() {
        Timber.d("setUpRecyclerView");

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.VERTICAL));
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(mContext, movie.getTitle() + " is selected", Toast.LENGTH_SHORT).show();

                // send data to activity
                listener.onMovieItemSelected(movie);
            }

            @Override
            public void onLongClick(View view, int position) {
                Movie movie = movieList.get(position);
                Toast.makeText(mContext, "Long click on : " + movie.getTitle(), Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void buildAndApplyAdapter() {
        Timber.d("buildAndApplyAdapter");
        mAdapter = new MoviesAdapter(mContext, movieList, this);
        recyclerView.setAdapter(mAdapter);
    }

    ///////////////////////////////////////////
    //
    //  Implements methods
    //
    ///////////////////////////////////////////
    @Override
    public void onMovieClicked(Movie movie) {
    }
}
