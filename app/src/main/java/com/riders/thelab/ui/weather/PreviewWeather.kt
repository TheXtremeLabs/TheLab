package com.riders.thelab.ui.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSuccess(viewModel: WeatherViewModel) {
    var expanded by remember { mutableStateOf(false) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var selectedText by remember { mutableStateOf("") }

    val cityName = remember { mutableStateOf("Torcy") }
    val country = remember { mutableStateOf("France") }
    val temperature = remember { mutableStateOf("2°") }

    TheLabTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TextField(
                value = viewModel.searchText,
                onValueChange = { viewModel.updateSearchText(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    },
                label = { Text("Search a Country, City,...") },
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
            ) {
                viewModel.suggestions.forEach { label ->
                    DropdownMenuItem(
                        modifier = Modifier,
                        onClick = {
                            selectedText = label.getString(label.getColumnIndexOrThrow("name"))
                        },
                        text = { Text(text = label.getString(label.getColumnIndexOrThrow("name"))) },
                    )
                }
            }
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
                        WeatherSuccess(viewModel)
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


///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviews
@Composable
fun PreviewWeatherContent() {
    val viewModel: WeatherViewModel = hiltViewModel()
    TheLabTheme {
        WeatherContent(viewModel = viewModel)
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherSuccess() {
    val viewModel: WeatherViewModel = hiltViewModel()
    TheLabTheme {
        WeatherSuccess(viewModel = viewModel)
    }
}