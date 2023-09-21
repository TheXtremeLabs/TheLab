package com.riders.thelab.ui.filterlistview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riders.thelab.core.data.local.model.WorldPopulation

class FilterListViewModel : ViewModel() {

    val populations: MutableLiveData<List<WorldPopulation>> = MutableLiveData()

    fun getPopulations(): LiveData<List<WorldPopulation>> {
        return populations
    }

    fun generatePopulationList() {

        // Generate sample data
        val rank: Array<String> = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
        val country: Array<String> = arrayOf(
            "China", "India", "United States",
            "Indonesia", "Brazil", "Pakistan", "Nigeria", "Bangladesh",
            "Russia", "Japan"
        )
        val population: Array<String> = arrayOf(
            "1,354,040,000", "1,210,193,422",
            "315,761,000", "237,641,326", "193,946,886", "182,912,000",
            "170,901,000", "152,518,015", "143,369,806", "127,360,000"
        )

        val list: MutableList<WorldPopulation> = ArrayList()

        for (i in rank.indices) {
            val wp = WorldPopulation(rank.get(i), country.get(i), population.get(i))
            // Binds all strings into an array
            list.add(wp)
        }

        populations.value = list
    }
}