package com.riders.thelab.ui.multipane.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.riders.thelab.core.utils.RecyclerItemClickListener
import com.riders.thelab.core.utils.UIManager
import com.riders.thelab.data.local.bean.MovieEnum
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.databinding.FragmentMultiPaneMainBinding
import com.riders.thelab.ui.multipane.MovieClickListener
import com.riders.thelab.ui.multipane.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MultiPaneMainFragment : Fragment(), MovieClickListener {

    private lateinit var viewBinding: FragmentMultiPaneMainBinding

    companion object {
        fun newInstance(): MultiPaneMainFragment {
            val args = Bundle()

            val fragment = MultiPaneMainFragment()
            fragment.arguments = args
            return fragment
        }
    }


    private var movieList: List<Movie>? = null
    private var mAdapter: MoviesAdapter? = null

    /**
     * passing data between fragments
     */
    private var listener: OnItemSelectedListener? = null

    interface OnItemSelectedListener {
        fun onMovieItemSelected(movie: Movie)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnItemSelectedListener) {
            listener = context
        } else {
            throw ClassCastException(
                context.toString()
                        + " must implement MultiPaneActivity.OnItemSelectedListener"
            )
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMultiPaneMainBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchData()
        setUpRecyclerView()
        buildAndApplyAdapter()
    }


    private fun fetchData() {
        Timber.d("fetchData")
        movieList = MovieEnum.getMovies()
    }

    private fun setUpRecyclerView() {
        Timber.d("setUpRecyclerView")
        val mLayoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity)
        viewBinding.rvMultiPaneMain.layoutManager = mLayoutManager
        viewBinding.rvMultiPaneMain.itemAnimator = DefaultItemAnimator()
        viewBinding.rvMultiPaneMain.addItemDecoration(
            DividerItemDecoration(
                requireActivity(),
                LinearLayoutManager.VERTICAL
            )
        )
        viewBinding.rvMultiPaneMain.addOnItemTouchListener(
            RecyclerItemClickListener(
                requireActivity(),
                viewBinding.rvMultiPaneMain,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View?, position: Int) {
                        val movieObject = movieList!![position]

                        UIManager.showActionInToast(
                            requireActivity(),
                            movieObject.title + " is selected"
                        )
                        // send data to activity
                        listener!!.onMovieItemSelected(movieObject)
                    }

                    override fun onLongClick(view: View?, position: Int) {
                        val movieObject = movieList!![position]

                        UIManager.showActionInToast(
                            requireActivity(),
                            "Long click on : " + movieObject.title
                        )
                    }
                })
        )
    }

    private fun buildAndApplyAdapter() {
        Timber.d("buildAndApplyAdapter")
        mAdapter = MoviesAdapter(requireActivity(), movieList!!, this)
        viewBinding.rvMultiPaneMain.adapter = mAdapter
    }

    ///////////////////////////////////////////
    //
    //  Implements methods
    //
    ///////////////////////////////////////////
    override fun onMovieClicked(movie: Movie) {
        listener?.onMovieItemSelected(movie)
    }
}