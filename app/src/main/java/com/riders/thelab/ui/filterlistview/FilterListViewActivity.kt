package com.riders.thelab.ui.filterlistview


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.data.local.model.WorldPopulation
import com.riders.thelab.databinding.ActivityFilterListviewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*

@AndroidEntryPoint
class FilterListViewActivity : AppCompatActivity(), TextWatcher {

    var mWorldPopulationList: ArrayList<WorldPopulation>? = null
    var adapter: FilterListViewAdapter? = null

    lateinit var viewBinding: ActivityFilterListviewBinding


    private val mFilterViewModel: FilterListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFilterListviewBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.activity_title_filter_list_view)

        setAdapter()


        mFilterViewModel
            .getPopulations()
            .observe(this, { list ->

                // Pass results to ListViewAdapter Class
                adapter = FilterListViewAdapter(this, list as MutableList<WorldPopulation>)

                // Binds the Adapter to the ListView
                viewBinding.lvFilterListview.adapter = adapter

                // Capture Text in EditText
                viewBinding.etFilterListviewSearch.addTextChangedListener(this)
            })
    }

    private fun setAdapter() {
        mFilterViewModel.generatePopulationList()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Timber.d("onTextChanged()")
    }

    override fun afterTextChanged(s: Editable?) {
        val text: String =
            viewBinding.etFilterListviewSearch.text.toString().toLowerCase(Locale.getDefault())
        adapter!!.filter(text)
    }
}