package com.riders.thelab.feature.weather.ui

import android.annotation.SuppressLint
import android.location.Address
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
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
import com.riders.thelab.core.common.utils.DateTimeUtils
import com.riders.thelab.core.common.utils.LabLocationManager
import com.riders.thelab.core.common.utils.toLocation
import com.riders.thelab.core.data.local.model.compose.WeatherCityUIState
import com.riders.thelab.core.data.local.model.compose.WeatherUIState
import com.riders.thelab.core.data.remote.dto.weather.DailyWeather
import com.riders.thelab.core.data.remote.dto.weather.OneCallWeatherResponse
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.Lottie
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.core.ui.compose.theme.md_theme_dark_primary
import com.riders.thelab.core.ui.compose.theme.md_theme_light_primary
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.data.WindDirection
import com.riders.thelab.feature.weather.core.component.TheLabTopAppBar
import com.riders.thelab.feature.weather.utils.Constants
import timber.log.Timber
import kotlin.math.roundToInt

///////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////
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
                    singleLine = true,
                    maxLines = 1
                    //readOnly = true,
                )

                ExposedDropdownMenu(
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() }),
                    expanded = viewModel.expanded,
                    onDismissRequest = { viewModel.expanded = false }
                ) {
                    viewModel.suggestions.take(10).forEachIndexed { _, city ->

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
                                    allowHardware(false)
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

@SuppressLint("UnusedContentLambdaTargetStateParameter")
@Composable
fun WeatherData(viewModel: WeatherViewModel) {

    val context = LocalContext.current

    val cityUIState by viewModel.weatherCityUiState.collectAsStateWithLifecycle()

    if (cityUIState is WeatherCityUIState.Success) {

        val weather = (cityUIState as WeatherCityUIState.Success).weather

        viewModel.getCityNameWithCoordinates(
            context.findActivity() as WeatherActivity,
            weather.latitude,
            weather.longitude
        )

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
                    allowHardware(false)
                    //transformations(RoundedCornersTransformation(32.dp.value))
                }
                .build(),
            placeholder = painterResource(R.drawable.logo_colors),
        )

        val address: Address = viewModel.weatherAddress!!

        // Load city name
        val cityName = address.locality
        val country = address.countryName

        // Temperatures
        val temperature =
            "${weather.currentWeather!!.temperature.roundToInt()} ${
                (context.findActivity() as WeatherActivity).getString(
                    R.string.degree_placeholder
                )
            }"

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
                                        text = weather.currentWeather!!.weather!![0].main,
                                        style = Typography.titleSmall,
                                        fontWeight = FontWeight.ExtraBold
                                    )
                                }
                            }

                            // Temperature row container
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
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

                                Button(onClick = { viewModel.updateMoreDataVisibility() }) {
                                    AnimatedContent(
                                        targetState = viewModel.isWeatherMoreDataVisible,
                                        label = "weather_visibility_animation"
                                    ) {
                                        Row(
                                            modifier = Modifier,
                                            horizontalArrangement = Arrangement.spacedBy(
                                                16.dp,
                                                Alignment.CenterHorizontally
                                            ),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = if (!viewModel.isWeatherMoreDataVisible) "Show More" else "Close Panel")
                                            Icon(
                                                imageVector = if (!viewModel.isWeatherMoreDataVisible) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                                                contentDescription = "more icon"
                                            )
                                        }
                                    }
                                }
                            }

                            AnimatedVisibility(visible = viewModel.isWeatherMoreDataVisible) {
                                WeatherMoreData(weather)
                            }

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
fun WeatherMoreData(weather: OneCallWeatherResponse) {
    val context = LocalContext.current
    val gridState = rememberLazyGridState()

    val realFeels =
        "${weather.currentWeather?.feelsLike?.roundToInt()} ${
            (context.findActivity() as WeatherActivity).resources.getString(
                R.string.degree_placeholder
            )
        }"

    val cloudiness = "${weather.currentWeather?.clouds.toString()} ${
        (context.findActivity() as WeatherActivity).resources.getString(R.string.percent_placeholder)
    }"

    val humidity = "${weather.currentWeather?.humidity.toString()} ${
        (context.findActivity() as WeatherActivity).resources.getString(R.string.percent_placeholder)
    }"

    val pressure: String = "${weather.currentWeather?.pressure.toString()} ${
        (context.findActivity() as WeatherActivity).resources.getString(R.string.pressure_unit_placeholder)
    }"

    // Wind
    val wind: String = "${weather.currentWeather?.windSpeed.toString()} ${
        (context.findActivity() as WeatherActivity).resources.getString(R.string.meter_unit_placeholder)
    }"

    val windDirection: WindDirection =
        WindDirection.getWindDirectionToTextualDescription(weather.currentWeather?.windDegree!!)

    // Build chart with hourly weather data
    // buildChart(hourlyWeather)

    TheLabTheme {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                Text(text = "Real Feels")
                Text(text = realFeels, fontWeight = FontWeight.ExtraBold)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterVertically
                    )
                ) {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_sunrise),
                        contentDescription = "sunrise icon",
                        colorFilter = ColorFilter.tint(
                            color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                            blendMode = BlendMode.SrcIn
                        )
                    )
                    Text(
                        text = DateTimeUtils.formatMillisToTimeHoursMinutes(
                            weather.timezone!!,
                            weather.currentWeather!!.sunrise
                        )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterVertically
                    )
                ) {
                    Image(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.ic_sunset),
                        contentDescription = "sunset icon",
                        colorFilter = ColorFilter.tint(
                            color = if (!isSystemInDarkTheme()) Color.Black else Color.White,
                            blendMode = BlendMode.SrcIn
                        )
                    )

                    Text(
                        text = DateTimeUtils.formatMillisToTimeHoursMinutes(
                            weather.timezone!!,
                            weather.currentWeather!!.sunset
                        )
                    )
                }
            }

            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                state = gridState,
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cloud),
                            contentDescription = "cloud icon"
                        )
                        Text(text = "Cloudiness: $cloudiness")
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pressure),
                            contentDescription = "pressure icon"
                        )
                        Text(text = "Pressure: $pressure")
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_wind),
                            contentDescription = "wind icon"
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(text = "Wind: $wind")
                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = windDirection.icon),
                                contentDescription = "wind direction icon"
                            )
                        }
                    }
                }
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_humidity),
                            contentDescription = "humidity icon"
                        )
                        Text(text = "humidity: $humidity")
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
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Weather trends for the next 5 days"
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = if (!isSystemInDarkTheme()) md_theme_light_primary else md_theme_dark_primary)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
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
                                allowHardware(false)
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

    val context = LocalContext.current

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
                Lottie(
                    modifier = Modifier.fillMaxSize(.5f),
                    rawResId = R.raw.error_rolling_dark_theme
                )

                Text("Error while getting weather for your location")

                Button(onClick = {
                    viewModel.retry()
                    (context.findActivity() as WeatherActivity).onResume()
                }) {
                    Text(stringResource(id = R.string.action_retry))
                }
            }
        }
    }
}

@Composable
fun WeatherContent(viewModel: WeatherViewModel, labLocationManager: LabLocationManager) {

    val context = LocalContext.current
    val weatherUIState by viewModel.weatherUiState.collectAsStateWithLifecycle()

    TheLabTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TheLabTopAppBar(
                    title = stringResource(id = R.string.activity_title_weather),
                    viewModel,
                    if (!viewModel.iconState) {
                        {
                            Timber.e("Unable to perform action due to location feature unavailable")
                            Toast.makeText(
                                context,
                                "Please make sure that the location setting is enabled",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        { (context.findActivity() as WeatherActivity).fetchCurrentLocation() }
                    }
                )
            }
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                when (weatherUIState) {
                    is WeatherUIState.Loading -> {
                        WeatherLoading(modifier = Modifier.align(Alignment.Center))
                    }

                    is WeatherUIState.SuccessWeatherData,
                    is WeatherUIState.Success -> {
                        WeatherSuccess(viewModel)
                    }

                    is WeatherUIState.Error -> {
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
    val context = LocalContext.current
    val viewModel: WeatherViewModel = hiltViewModel()
    val labLocationManager = LabLocationManager(context)
    TheLabTheme {
        WeatherContent(viewModel = viewModel, labLocationManager = labLocationManager)
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherSuccess() {
    val viewModel: WeatherViewModel = hiltViewModel()
    viewModel.updateUIState(WeatherUIState.SuccessWeatherData(true))
    viewModel.updateWeatherCityUIState(WeatherCityUIState.Success(OneCallWeatherResponse.getMockResponse()))

    TheLabTheme {
        WeatherSuccess(viewModel = viewModel)
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherData() {
    val viewModel: WeatherViewModel = hiltViewModel()
    viewModel.updateUIState(WeatherUIState.SuccessWeatherData(true))
    viewModel.updateWeatherCityUIState(WeatherCityUIState.Success(OneCallWeatherResponse.getMockResponse()))
    TheLabTheme {
        WeatherData(viewModel = viewModel)
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherMoreData() {
    TheLabTheme {
        WeatherMoreData(OneCallWeatherResponse.getMockResponse())
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherDailyForecast() {
    val viewModel: WeatherViewModel = hiltViewModel()
    TheLabTheme {
        WeatherDailyForecast(viewModel, listOf())
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