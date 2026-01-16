package com.riders.thelab.feature.flightaware.ui.search

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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.riders.thelab.core.ui.compose.base.BaseComponentActivity
import com.riders.thelab.core.ui.compose.base.observeLifecycleEvents
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.flightaware.utils.Constants
import com.riders.thelab.feature.flightaware.utils.FlightNavigator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class SearchFlightActivity : BaseComponentActivity() {

    private val mViewModel: SearchFlightViewModel by viewModels<SearchFlightViewModel>()

    private var mFlightNavigator: FlightNavigator? = null
    private val locale = Locale.getDefault()
    private var currentDate: NotBlankString? = null

    ///////////////////////////////
    //
    // OVERRIDE
    //
    ///////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initVariables()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {

                setContent {
                    mViewModel.observeLifecycleEvents(LocalLifecycleOwner.current.lifecycle)

                    val theme: AppTheme by mViewModel
                        .theme
                        .collectAsStateWithLifecycle()
                    val isDarkTheme: Boolean? by mViewModel
                        .isDarkMode
                        .collectAsStateWithLifecycle()

                    val uiState by mViewModel.searchFlightUiState.collectAsStateWithLifecycle()

                    TheLabTheme(theme = theme, darkTheme = isDarkTheme ?: isSystemInDarkTheme()) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SearchFlightsContent(
                                theme = theme,
                                darkTheme = isDarkTheme ?: isSystemInDarkTheme(),
                                currentDate = currentDate!!,
                                uiState = uiState
                            ) { event ->
                                when (event) {
                                    is UiEvent.OnFlightClicked -> mFlightNavigator?.launchFlightDetailActivity(
                                        event.flightItem
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause()")
    }

    override fun onResume() {
        super.onResume()
        Timber.d("onResume()")
    }

    override fun backPressed() {
        finish()
    }


    ///////////////////////////////
    //
    // CLASS METHODS
    //
    ///////////////////////////////
    fun initVariables() {
        Timber.d("initVariables()")

        mFlightNavigator = FlightNavigator(this)
        setupCurrentDate()
        initViewModels()
    }
    @OptIn(ExperimentalKotoolsTypesApi::class)
    fun setupCurrentDate() {
        Timber.d("setupCurrentDate()")
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT_PATTERN, locale)
        val formattedDate = now.format(formatter)

        currentDate = formattedDate.toNotBlankString().getOrThrow()
    }

    private fun initViewModels() {
        Timber.d("initViewModels()")
        mViewModel.initWeakReference(this@SearchFlightActivity)
        mViewModel.getBundle(intent)
    }

}