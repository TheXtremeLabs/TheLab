package com.riders.thelab.ui.filterlistview


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.riders.thelab.R
import com.riders.thelab.databinding.ActivityFilterListviewDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterListViewDetailActivity : AppCompatActivity() {

    lateinit var viewBinding: ActivityFilterListviewDetailBinding

    private var rank: String? = null
    private var country: String? = null
    private var population: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityFilterListviewDetailBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.activity_title_filter_list_view_detail)

        getBundle()
        loadData()
    }

    private fun getBundle() {

        // Retrieve data from MainActivity on item click event
        val i = intent
        // Get the results of rank
        rank = i.getStringExtra("rank")
        // Get the results of country
        country = i.getStringExtra("country")
        // Get the results of population
        population = i.getStringExtra("population")
    }

    private fun loadData() {

        // Load the results into the TextViews
        viewBinding.rank.text = rank
        viewBinding.country.text = country
        viewBinding.population.text = population
    }
}