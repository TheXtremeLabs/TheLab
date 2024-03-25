package com.riders.thelab.feature.flightaware.ui.flight

import androidx.activity.viewModels
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity

class FlightDetailActivity : BaseComponentActivity() {

    private val mViewModel: FlightDetailViewModel by viewModels<FlightDetailViewModel>()

    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun backPressed() {
       finish()
    }
}