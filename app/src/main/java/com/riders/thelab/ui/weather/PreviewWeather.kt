package com.riders.thelab.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.*
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.component.TheLabTopAppBar
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.data.local.model.compose.WeatherUIState.*

@DevicePreviews
@Composable
fun WeatherLoading(modifier: Modifier = Modifier) {
    TheLabTheme {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@DevicePreviews
@Composable
fun WeatherSuccess() {
    val cityName = remember { mutableStateOf("Torcy") }
    val country = remember { mutableStateOf("France") }
    val temperature = remember { mutableStateOf("2°") }

    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(cityName.value)
            Text(country.value)
            Text(temperature.value)
        }

    }
}

@Composable
fun WeatherError(modifier: Modifier, viewModel: WeatherViewModel) {

    val composition by
    rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.error_rolling_dark_theme))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
    )

    TheLabTheme {
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
                LottieAnimation(
                    modifier = Modifier.fillMaxSize(.5f),
                    composition = composition,
                    progress = { progress }
                )

                Text("Error while getting weather for your location")

                Button(onClick = { viewModel.retry() }) {
                    Text("Retry")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherContent(viewModel: WeatherViewModel) {

    val weatherUIState by viewModel.weatherUiState.collectAsStateWithLifecycle()

    TheLabTheme {

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TheLabTopAppBar(title = stringResource(id = R.string.activity_title_weather)) }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                when (weatherUIState) {
                    is Loading -> {
                        WeatherLoading(modifier = Modifier.align(Alignment.Center))
                    }
                    is SuccessWeatherData,
                    is Success -> {
                        WeatherSuccess()
                    }
                    is Error -> {
                        WeatherError(
                            modifier = Modifier.align(Alignment.Center),
                            viewModel = viewModel
                        )
                    }
                    else -> {}
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherContent() {
    val viewModel: WeatherViewModel = hiltViewModel()
    TheLabTheme {
        WeatherContent(viewModel = viewModel)
    }
}