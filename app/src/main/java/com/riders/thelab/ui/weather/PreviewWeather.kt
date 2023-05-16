package com.riders.thelab.ui.weather

import android.location.Address
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import com.riders.thelab.R
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.component.TheLabTopAppBar
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.ui.theme.Typography
import com.riders.thelab.core.compose.utils.findActivity
import com.riders.thelab.core.utils.toLocation
import com.riders.thelab.data.local.bean.WindDirection
import com.riders.thelab.data.local.model.compose.WeatherCityUIState
import com.riders.thelab.data.local.model.compose.WeatherUIState.*
import com.riders.thelab.data.remote.dto.weather.DailyWeather
import com.riders.thelab.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.utils.Constants
import com.riders.thelab.utils.DateTimeUtils
import timber.log.Timber
import kotlin.math.roundToInt

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

    val focusManager: FocusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (viewModel.expanded)
        Icons.Filled.ArrowDropUp //it requires androidx.compose.material:material-icons-extended
    else
        Icons.Filled.ArrowDropDown

    TheLabTheme {
        Column(modifier = Modifier.fillMaxSize()) {

            // Weather city search field
            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = viewModel.expanded,
                onExpandedChange = {
                    viewModel.expanded = !viewModel.expanded
                }
            ) {
                TextField(
                    value = viewModel.searchText,
                    onValueChange = { viewModel.updateSearchText(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onGloballyPositioned { coordinates ->
                            //This value is used to assign to the DropDown the same width
                            textFieldSize = coordinates.size.toSize()
                        }
                        .focusRequester(focusRequester)
                        .menuAnchor(),
                    label = { Text("Search a Country, City,...") },
                    trailingIcon = {
                        Icon(imageVector = icon,
                            contentDescription = "contentDescription",
                            Modifier.clickable { viewModel.expanded = !viewModel.expanded }
                        )
                    },
                    //readOnly = true,
                )

                ExposedDropdownMenu(
                    expanded = viewModel.expanded,
                    onDismissRequest = { viewModel.expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    viewModel.suggestions.take(10).forEachIndexed { index, city ->

                        val countryURL: String =
                            (Constants.BASE_ENDPOINT_WEATHER_FLAG
                                    + city.country.lowercase()
                                    + Constants.WEATHER_FLAG_PNG_SUFFIX)

                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data(countryURL)
                                .apply {
                                    crossfade(true)
                                    allowHardware(true)
                                    //transformations(RoundedCornersTransformation(32.dp.value))
                                }
                                .build(),
                            placeholder = painterResource(R.drawable.logo_colors),
                        )

                        DropdownMenuItem(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.updateSearchText("${city.name}, ${city.country}")
                                viewModel.expanded = false
                                focusManager.clearFocus(true)
                                viewModel.fetchWeather((city.latitude to city.longitude).toLocation())
                            },
                            text = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = "${city.name}, ${city.country}"
                                    )

                                    Image(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp)),
                                        painter = painter,
                                        contentDescription = "palette image wth coil",
                                        contentScale = ContentScale.Crop,
                                    )
                                }
                            },
                        )
                    }
                }
            }

            // Weather city data to display
            WeatherData(viewModel)
        }
    }
}

@Composable
fun WeatherData(viewModel: WeatherViewModel) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val cityUIState by viewModel.weatherCityUiState.collectAsStateWithLifecycle()

    if (cityUIState is WeatherCityUIState.Success) {
        val weather = (cityUIState as WeatherCityUIState.Success).weather

        val painter = rememberAsyncImagePainter(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(
                    WeatherUtils.getWeatherIconFromApi(
                        weather
                            .currentWeather
                            ?.weather!![0]
                            .icon
                    )
                )
                .apply {
                    crossfade(true)
                    allowHardware(true)
                    //transformations(RoundedCornersTransformation(32.dp.value))
                }
                .build(),
            placeholder = painterResource(R.drawable.logo_colors),
        )
        val state = painter.state

        val address: Address =
            viewModel.getCityNameWithCoordinates(
                context.findActivity() as WeatherActivity,
                weather.latitude,
                weather.longitude
            )!!

        // Load city name
        val cityName = address.locality
        val country = address.countryName

        // Temperatures
        val temperature =
            "${weather.currentWeather.temperature.roundToInt()} ${
                (context.findActivity() as WeatherActivity).getString(
                    R.string.degree_placeholder
                )
            }"

        val realFeels =
            "${weather.currentWeather.feelsLike.roundToInt()} ${
                (context.findActivity() as WeatherActivity).resources.getString(
                    R.string.degree_placeholder
                )
            }"

        val sunset =
            DateTimeUtils.formatMillisToTimeHoursMinutes(
                weather.timezone!!,
                weather.currentWeather.sunset
            )

        // Build chart with hourly weather data
        // buildChart(hourlyWeather)

        val cloudiness: String = weather.currentWeather.clouds
            .toString() + " " + (context.findActivity() as WeatherActivity).resources.getString(R.string.percent_placeholder)

        val humidity: String = weather.currentWeather.humidity
            .toString() + " " + (context.findActivity() as WeatherActivity).resources.getString(R.string.percent_placeholder)

        val pressure: String = weather.currentWeather.pressure
            .toString() + " " + (context.findActivity() as WeatherActivity).resources.getString(R.string.pressure_unit_placeholder)

        // Wind
        val wind: String =
            (weather.currentWeather.windSpeed.toString() + " "
                    + (context.findActivity() as WeatherActivity).resources.getString(R.string.meter_unit_placeholder))
        val windDirection: WindDirection = WindDirection.getWindDirectionToTextualDescription(
            weather.currentWeather.windDegree
        )

        weather.hourlyWeather?.let { viewModel.getMaxMinTemperature(it) }

        TheLabTheme {
            AnimatedVisibility(visible = cityUIState is WeatherCityUIState.Success) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                // Weather icon
                                Image(
                                    modifier = Modifier
                                        .size(72.dp)
                                        .clip(RoundedCornerShape(12.dp)),
                                    painter = painter,
                                    contentDescription = "weather icon wth coil",
                                    contentScale = ContentScale.Fit,
                                )

                                // Colum with city name country and weather state
                                Column(
                                    horizontalAlignment = Alignment.End,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(text = "$cityName, $country")
                                    Text(
                                        text = weather.currentWeather.weather[0].main,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            // Temperature row container
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier) {
                                    // current temperature
                                    Text(
                                        text = temperature,
                                        style = Typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    // Min | Max Temperatures
                                    Row(
                                        modifier = Modifier,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = viewModel.cityMaxTemp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "|"
                                        )
                                        Text(
                                            text = viewModel.cityMinTemp
                                        )
                                    }
                                }
                            }

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Weather trends for the next 5 days"
                            )

                            // Forecast
                            WeatherDailyForecast(
                                viewModel = viewModel,
                                dailyWeatherList = weather.dailyWeather!!
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(id = R.string.weather_data_provided_by),
                            fontSize = 12.sp
                        )
                        Image(
                            modifier = Modifier.height(28.dp),
                            painter = painterResource(id = R.drawable.openweathermap_logo_white),
                            contentDescription = "open weather icon"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherDailyForecast(viewModel: WeatherViewModel, dailyWeatherList: List<DailyWeather>) {
    val listState = rememberLazyListState()

    TheLabTheme {
        Card(modifier = Modifier.fillMaxWidth()) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    items = dailyWeatherList.subList(
                        1,
                        dailyWeatherList.size - 2
                    )
                ) { dailyWeather ->

                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(
                                WeatherUtils.getWeatherIconFromApi(dailyWeather.weather[0].icon)
                            )
                            .apply {
                                crossfade(true)
                                allowHardware(true)
                            }
                            .build(),
                        placeholder = painterResource(R.drawable.logo_colors),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Day of the week
                        Text(
                            text = viewModel.getDayFromTime(dailyWeather.dateTimeUTC),
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icons with temperature
                            // Weather icon
                            Image(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                painter = painter,
                                contentDescription = "weather icon wth coil",
                                contentScale = ContentScale.Crop,
                            )
                            // current temperature
                            Text(
                                text = "${dailyWeather.temperature.max.toInt()}°",
                                fontWeight = FontWeight.Bold
                            )
                            // current temperature
                            Text(
                                text = "${dailyWeather.temperature.min.toInt()}°"
                            )
                        }
                    }
                }
            }
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

    val context = LocalContext.current
    val weatherUIState by viewModel.weatherUiState.collectAsStateWithLifecycle()

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    title = stringResource(id = R.string.activity_title_weather),
                    Icons.Filled.GpsFixed
                ) { (context.findActivity() as WeatherActivity).fetchCurrentLocation() }
            }
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

                    else -> {
                        Timber.e("Else branch")
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
    viewModel.updateUIState(SuccessWeatherData(true))
    TheLabTheme {
        WeatherSuccess(viewModel = viewModel)
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherData() {
    val viewModel: WeatherViewModel = hiltViewModel()
    viewModel.updateUIState(SuccessWeatherData(true))
    viewModel.updateWeatherCityUIState(WeatherCityUIState.Success(OneCallWeatherResponse.getMockResponse()))
    TheLabTheme {
        WeatherData(viewModel = viewModel)
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherError() {
    val viewModel: WeatherViewModel = hiltViewModel()
    TheLabTheme {
        WeatherError(modifier = Modifier, viewModel = viewModel)
    }
}