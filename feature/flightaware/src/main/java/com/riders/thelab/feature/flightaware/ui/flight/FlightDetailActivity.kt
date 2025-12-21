package com.riders.thelab.feature.flightaware.ui.flight

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.data.local.IUiRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FlightDetailActivity : BaseComponentActivity() {

    private val mViewModel: FlightDetailViewModel by viewModels<FlightDetailViewModel>()

    @Inject
    lateinit var uiRepository: IUiRepository

    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel.getBundle(intent)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                setContent {
                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val uiState by mViewModel.flightDetailUiState.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            FlightDetailContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                uiState = uiState
                            )
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