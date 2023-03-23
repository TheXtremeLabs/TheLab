package com.riders.thelab.ui.filterlistview


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.data.local.model.WorldPopulation
import com.riders.thelab.databinding.ActivityFilterListviewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class FilterListViewActivity : AppCompatActivity(), TextWatcher {

    private var _viewBinding: ActivityFilterListviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    private val mFilterViewModel: FilterListViewModel by viewModels()

    var mWorldPopulationList: ArrayList<WorldPopulation>? = null
    lateinit var adapter: FilterListViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityFilterListviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setAdapter()

        mFilterViewModel
            .getPopulations()
            .observe(this, { list ->

                // Pass results to ListViewAdapter Class
                adapter = FilterListViewAdapter(this, list as MutableList<WorldPopulation>)

                // Binds the Adapter to the ListView
                binding.lvFilterListview.adapter = adapter

                // Capture Text in EditText
                binding.etFilterListviewSearch.addTextChangedListener(this)
            })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy()")
        _viewBinding = null
    }


    private fun setAdapter() {
        mFilterViewModel.generatePopulationList()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Timber.d("onTextChanged()")
    }

    override fun afterTextChanged(s: Editable?) {
        val text: String = binding.etFilterListviewSearch.text.toString().lowercase()
        adapter.filter(text)
    }
}