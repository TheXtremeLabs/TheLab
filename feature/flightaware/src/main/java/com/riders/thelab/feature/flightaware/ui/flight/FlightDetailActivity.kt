package com.riders.thelab.feature.flightaware.ui.flight

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import kotlinx.coroutines.launch

class FlightDetailActivity : BaseComponentActivity() {

    private val mViewModel: FlightDetailViewModel by viewModels<FlightDetailViewModel>()

    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.getBundle(intent)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {

                    TheLabTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AnimatedContent(
                                modifier = Modifier.fillMaxSize(),
                                targetState = null != mViewModel.flight,
                                contentAlignment = Alignment.Center,
                                label = "flight animation content"
                            ) { targetState ->
                                if (!targetState) {
                                    LabLoader(modifier = Modifier.size(40.dp))
                                } else {
                                    FlightDetailContent(
                                        flight = mViewModel.flight!!,
                                        uiEvent = mViewModel::onEvent
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun backPressed() {
        finish()
    }

    companion object {
        const val EXTRA_FLIGHT: String = "EXTRA_FLIGHT"
    }
}