package com.riders.thelab.feature.flightaware.utils

import android.content.Context
import android.content.Intent
import com.riders.thelab.core.data.local.model.flight.SearchFlightModel
import com.riders.thelab.feature.flightaware.data.local.model.SearchFlightType
import com.riders.thelab.feature.flightaware.ui.airport.AirportSearchActivity
import com.riders.thelab.feature.flightaware.ui.airport.AirportSearchDetailActivity
import com.riders.thelab.feature.flightaware.ui.flight.FlightDetailActivity
import com.riders.thelab.feature.flightaware.ui.search.SearchFlightActivity
import com.riders.thelab.feature.flightaware.utils.Constants.EXTRA_SEARCH_TYPE
import timber.log.Timber

class FlightNavigator(private val context: Context) {

    fun launchSearchFlightActivity(
        searchFlightType: SearchFlightType,
        flightId: String? = null,
        flightRoute: Pair<String, String>? = null
    ) = runCatching {
        Intent(context, SearchFlightActivity::class.java)
            .apply {
                this.putExtra(EXTRA_SEARCH_TYPE, searchFlightType)
                flightId?.let { this.putExtra(Constants.EXTRA_SEARCH_TYPE_FLIGHT_NUMBER, it) }
                flightRoute?.let { this.putExtra(Constants.EXTRA_SEARCH_TYPE_FLIGHT_ROUTE, it) }
            }
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("launchSearchFlightActivity() | error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .onSuccess { intent -> context.startActivity(intent) }

    fun launchFlightDetailActivity(searchFlight: SearchFlightModel) = runCatching {
        Intent(context, FlightDetailActivity::class.java)
            .apply {
                this.putExtra(Constants.EXTRA_FLIGHT, searchFlight)
            }
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("launchFlightDetailActivity() | error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .onSuccess { intent -> context.startActivity(intent) }

    fun launchAirportSearchActivity() = runCatching {
        Intent(context, AirportSearchActivity::class.java)
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("launchAirportSearchActivity() | error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .onSuccess { intent -> context.startActivity(intent) }

    fun launchAirportSearchDetailActivity(airportID: String) = runCatching {
        Intent(context, AirportSearchDetailActivity::class.java)
            .apply { this.putExtra(Constants.EXTRA_AIRPORT_ID, airportID) }
    }
        .onFailure { exception ->
            exception.printStackTrace()
            Timber.e("launchAirportSearchDetailActivity() | error caught with message: ${exception.message} (class: ${exception.javaClass.canonicalName})")
        }
        .onSuccess { intent -> context.startActivity(intent) }

}