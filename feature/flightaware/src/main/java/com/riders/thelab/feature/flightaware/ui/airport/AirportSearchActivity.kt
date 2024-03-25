package com.riders.thelab.feature.flightaware.ui.airport

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.launch

class AirportSearchActivity : BaseComponentActivity() {

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AirportSearchContent()
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }


    /////////////////////////////////////
    //
    // CLASS METHODS
    //
    /////////////////////////////////////
    fun launchAirportDetail(airportID: String) =
        Intent(this, AirportSearchDetailActivity::class.java)
            .apply { this.putExtra(AirportSearchDetailActivity.EXTRA_AIRPORT_ID, airportID) }
            .run { startActivity(this) }
}