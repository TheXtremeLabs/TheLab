package com.riders.thelab.data.local.model

data class WorldPopulation(val rank: String, val country: String, val population: String) {
    override fun toString(): String {
        return "WorldPopulation(rank='$rank', country='$country', population='$population')"
    }
}
