package com.riders.thelab.feature.weather.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.model.compose.weather.WeatherDataState
import com.riders.thelab.core.data.local.model.compose.weather.WeatherUIState
import com.riders.thelab.core.data.local.model.weather.CityModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.component.loading.LabLoader
import com.riders.thelab.core.ui.compose.component.toolbar.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.utils.UIManager
import timber.log.Timber
import java.util.UUID


///////////////////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////////////////
@Composable
fun WeatherLoading(theme: AppTheme, darkTheme: Boolean, modifier: Modifier = Modifier) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        BoxWithConstraints(
            modifier = Modifier
                .size(40.dp)
                .then(modifier),
            contentAlignment = Alignment.Center
        ) {
            Lottie(
                modifier = Modifier.size(this.maxWidth, this.maxHeight),
                url = "https://assets2.lottiefiles.com/packages/lf20_kk62um5v.json"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSuccess(
    theme: AppTheme, darkTheme: Boolean,
    weatherUiState: WeatherUIState,
    searchMenuExpanded: Boolean,
    searchCityQuery: String,
    suggestions: List<CityModel>,
    isWeatherMoreDataVisible: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Weather city search field
            WeatherCitySearchField(
                theme = theme,
                darkTheme = darkTheme,
                suggestions = suggestions,
                searchCityQuery = searchCityQuery,
                onSearchTextChange = { uiEvent.invoke(UiEvent.OnUpdateSearchCityQuery(it)) },
                searchMenuExpanded = searchMenuExpanded,
                onUpdateSearchMenuExpanded = {
                    uiEvent.invoke(UiEvent.OnUpdateSearchMenuExpanded(it))
                },
                onFetchWeatherRequest = { latitude, longitude ->
                    uiEvent.invoke(UiEvent.OnFetchWeatherForCity(latitude, longitude))
                },
                onDismissSearch = {}
            )

            // Weather city data to display
            WeatherMainCityContent(
                theme = theme, darkTheme = darkTheme,
                weatherUIState = weatherUiState,
                isWeatherMoreDataVisible = isWeatherMoreDataVisible,
                uiEvent = uiEvent
            )
        }
    }
}

@Composable
fun WeatherError(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier,
    onRetryButtonClicked: () -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Lottie(
                    modifier = Modifier.fillMaxSize(.5f),
                    rawResId = R.raw.error_rolling_dark_theme
                )

                Text("Error while getting weather for your location")

                Button(onClick = onRetryButtonClicked) {
                    Text(stringResource(id = R.string.action_retry))
                }
            }
        }
    }
}

@Composable
fun WeatherContent(
    theme: AppTheme, darkTheme: Boolean,
    weatherDataState: WeatherDataState,
    weatherUiState: WeatherUIState,
    iconState: Boolean,
    searchMenuExpanded: Boolean,
    searchCityQuery: String,
    suggestions: List<CityModel>,
    isWeatherMoreDataVisible: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    val context = LocalContext.current

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    theme = theme,
                    title = stringResource(id = R.string.activity_title_weather),
                    iconState = iconState,
                    actionBlock = {
                        if (!iconState) {
                            Timber.e("Unable to perform action due to location feature unavailable")

                            UIManager.showToast(
                                context,
                                "Please make sure that the location setting is enabled"
                            )
                        } else {
                            uiEvent.invoke(UiEvent.OnMyLocationClicked)
                        }
                    }
                )
            }) { contentPadding ->
            AnimatedContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                targetState = weatherDataState
            ) { targetState: WeatherDataState ->
                when (targetState) {
                    is WeatherDataState.None,
                    is WeatherDataState.Loading -> {
                        // Loading State
                        LabLoader(modifier = Modifier.size(56.dp))
                        //WeatherLoading(theme = theme, darkTheme = darkTheme)
                    }

                    is WeatherDataState.Error -> {
                        // Error State
                        WeatherError(
                            theme = theme,
                            darkTheme = darkTheme,
                            modifier = Modifier.fillMaxSize(),
                            onRetryButtonClicked = { uiEvent.invoke(UiEvent.OnRetryRequest) }
                        )
                    }

                    is WeatherDataState.SuccessWeatherData -> {
                        // Success State
                        WeatherSuccess(
                            theme = theme,
                            darkTheme = darkTheme,
                            weatherUiState = weatherUiState,
                            searchMenuExpanded = searchMenuExpanded,
                            searchCityQuery = searchCityQuery,
                            suggestions = suggestions,
                            isWeatherMoreDataVisible = isWeatherMoreDataVisible,
                            uiEvent = uiEvent
                        )
                    }
                }
            }
        }
    }
}

///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewWeatherLoading(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        WeatherLoading(theme = appTheme, darkTheme = isSystemInDarkTheme())
    }
}

@DevicePreviews
@Composable
private fun PreviewWeatherError(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        WeatherError(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier,
            onRetryButtonClicked = {}
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewWeatherContent(@PreviewParameter(PreviewProviderWeatherDataState::class) dataState: WeatherDataState) {
    val weatherUIState: WeatherUIState = PreviewProviderWeatherUIState().values.toList()[0]

    TheLabTheme(theme = AppTheme.Default) {
        WeatherContent(
            theme = AppTheme.Default, darkTheme = isSystemInDarkTheme(),
            weatherDataState = dataState,
            weatherUiState = weatherUIState,
            iconState = true,
            searchMenuExpanded = true,
            searchCityQuery = "Pa",
            suggestions = listOf(
                CityModel(
                    id = 1,
                    uuid = UUID.randomUUID().toString(),
                    name = "Johanesburg",
                    state = "",
                    country = "South Africa",
                    longitude = 48.3535,
                    latitude = 3.58978
                )
            ),
            isWeatherMoreDataVisible = true,
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewWeatherContentJohannesburg(@PreviewParameter(PreviewProviderWeatherDataState::class) dataState: WeatherDataState) {
    val weatherUIState: WeatherUIState = PreviewProviderWeatherUIState().values.toList()[0]

    TheLabTheme(theme = AppTheme.Default) {
        WeatherContent(
            theme = AppTheme.Default, darkTheme = isSystemInDarkTheme(),
            weatherDataState = dataState,
            weatherUiState = weatherUIState,
            iconState = true,
            searchMenuExpanded = true,
            searchCityQuery = "Johannesbu",
            suggestions = listOf(
                CityModel(
                    id = 1,
                    uuid = UUID.randomUUID().toString(),
                    name = "Johanesburg",
                    state = "",
                    country = "South Africa",
                    longitude = 48.3535,
                    latitude = 3.58978
                )
            ),
            isWeatherMoreDataVisible = true,
        ) {}
    }
}