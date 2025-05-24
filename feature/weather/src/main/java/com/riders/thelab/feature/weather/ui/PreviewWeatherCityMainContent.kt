package com.riders.thelab.feature.weather.ui

import android.location.Address
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.riders.thelab.core.common.utils.DateTimeUtils
import com.riders.thelab.core.data.local.model.compose.weather.WeatherUIState
import com.riders.thelab.core.data.local.model.weather.WeatherModel
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.theme.Typography
import com.riders.thelab.core.ui.data.local.bean.WindDirection
import kotlin.math.roundToInt


///////////////////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////////////////
@Composable
fun WeatherMoreData(
    theme: AppTheme,
    darkTheme: Boolean,
    weather: WeatherModel,
    modifier: Modifier = Modifier
) {
    val gridState = rememberLazyGridState()

    val realFeels =
        "${weather.temperature?.realFeels?.roundToInt()} ${stringResource(R.string.degree_placeholder)}"
    val cloudiness = "${weather.clouds} ${stringResource(R.string.percent_placeholder)}"
    val humidity = "${weather.humidity} ${stringResource(R.string.percent_placeholder)}"
    val pressure = "${weather.pressure} ${stringResource(R.string.pressure_unit_placeholder)}"
    // Wind
    val wind = "${weather.windSpeed.toString()} ${stringResource(R.string.meter_unit_placeholder)}"

    val windDirection: WindDirection =
        WindDirection.getWindDirectionToTextualDescription(weather.windDegree)

    // Build chart with hourly weather data
    // buildChart(hourlyWeather)

    val sunrise: String = DateTimeUtils.formatMillisToTimeHoursMinutes(
        weather.timezone!!,
        weather.sunrise
    )

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(modifier = Modifier.then(modifier)) {
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
                    horizontalArrangement = Arrangement.spacedBy(
                        24.dp,
                        Alignment.CenterHorizontally
                    )
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
                            text = sunrise
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
                                weather.sunset
                            )
                        )
                    }
                }

                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(56.dp, 300.dp),
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
}

@Composable
fun WeatherSunriseSunset(
    theme: AppTheme,
    darkTheme: Boolean,
    sunrise: String,
    sunset: String,
    modifier: Modifier = Modifier
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(modifier = Modifier.then(modifier)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(48.dp, Alignment.CenterHorizontally)
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
                    Text(text = sunrise)
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

                    Text(text = sunset)
                }
            }
        }
    }
}


@Composable
fun WeatherEnvironmentItem(
    theme: AppTheme,
    darkTheme: Boolean,
    @DrawableRes cardTitleIcon: Int,
    cardTitle: String,
    content: @Composable () -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(),
                        painter = painterResource(cardTitleIcon),
                        contentDescription = null
                    )
                    Text(text = cardTitle, fontSize = 14.sp)
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    content.invoke()
                }
            }
        }
    }
}


@Composable
fun WeatherEnvironment(
    theme: AppTheme,
    darkTheme: Boolean,
    cloudiness: Int,
    humidity: Int,
    pressure: Int,
    windSpeed: Double,
    windDegree: Int
) {
    val lazyGridState = rememberLazyGridState()

    val windDirection: WindDirection =
        WindDirection.getWindDirectionToTextualDescription(windDegree)

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(56.dp, 250.dp)
                .padding(horizontal = 16.dp),
            state = lazyGridState,
            columns = GridCells.Fixed(2),
            //columns = GridCells.Adaptive(128.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            userScrollEnabled = false,
        ) {
            // Cloudiness
            item {
                WeatherEnvironmentItem(
                    theme = theme,
                    darkTheme = darkTheme,
                    cardTitleIcon = R.drawable.ic_cloud,
                    cardTitle = "Cloudiness",
                    content = {
                        Text(
                            text = "$cloudiness ${stringResource(R.string.percent_placeholder)}",
                            fontWeight = FontWeight.W700
                        )
                    }
                )
            }

            // Pressure
            item {
                WeatherEnvironmentItem(
                    theme = theme,
                    darkTheme = darkTheme,
                    cardTitleIcon = R.drawable.ic_pressure,
                    cardTitle = "Pressure",
                    content = {
                        Text(
                            text = "$pressure ${stringResource(R.string.pressure_unit_placeholder)}",
                            fontWeight = FontWeight.W700
                        )
                    }
                )
            }

            // Wind
            item {
                WeatherEnvironmentItem(
                    theme = theme,
                    darkTheme = darkTheme,
                    cardTitleIcon = R.drawable.ic_wind,
                    cardTitle = "Wind",
                    content = {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = "${windSpeed.toString()} ${stringResource(R.string.meter_unit_placeholder)}",
                                style = TextStyle(fontWeight = FontWeight.W700)
                            )

                            Icon(
                                modifier = Modifier.size(20.dp),
                                painter = painterResource(id = windDirection.icon),
                                contentDescription = "wind direction icon"
                            )
                        }
                    }
                )
            }

            // Humidity
            item {
                WeatherEnvironmentItem(
                    theme = theme,
                    darkTheme = darkTheme,
                    cardTitleIcon = R.drawable.ic_humidity,
                    cardTitle = "Humidity",
                    content = {
                        Text(
                            text = "$humidity ${stringResource(R.string.percent_placeholder)}",
                            fontWeight = FontWeight.W700
                        )
                    }
                )
            }
        }
    }
}


@Composable
fun BlurredWeatherIconBackground(painter: Painter) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .blur(25.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Weather icon
        Image(
            modifier = Modifier
                .size(96.dp)
                .clip(RoundedCornerShape(12.dp)),
            painter = painter,
            contentDescription = "weather icon wth coil",
            contentScale = ContentScale.Fit,
        )
    }
}

@Composable
fun WeatherMainCityContent(
    theme: AppTheme, darkTheme: Boolean,
    weatherUIState: WeatherUIState,
    isWeatherMoreDataVisible: Boolean,
    uiEvent: (UiEvent) -> Unit
) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        AnimatedContent(
            modifier = Modifier.fillMaxWidth(),
            targetState = weatherUIState
        ) { targetState: WeatherUIState ->
            when (targetState) {
                is WeatherUIState.None -> Box(modifier = Modifier)
                is WeatherUIState.Success -> {

                    val mainListState = rememberLazyListState()

                    val weather = targetState.weather
                    val address: Address? = targetState.weather.address
                    val cityName = targetState.weather.address?.locality
                    val country = address?.countryName

                    val painter = rememberAsyncImagePainter(
                        model = ImageRequest
                            .Builder(LocalContext.current)
                            .data(weather.weatherIconUrl.toString())
                            .apply {
                                crossfade(true)
                                allowHardware(false)
                                //transformations(RoundedCornersTransformation(32.dp.value))
                            }
                            .build(),
                        placeholder = painterResource(R.drawable.logo_colors),
                    )

                    // Temperatures
                    val temperature =
                        "${weather.temperature?.temperature?.roundToInt()} ${
                            stringResource(R.string.degree_placeholder)
                        }"

                    // weather.hourlyWeather?.let { onGetMaxMinTemperature(it) }

                    AnimatedVisibility(visible = true) {
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            state = mainListState,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(
                                16.dp,
                                Alignment.CenterVertically
                            ),
                        ) {
                            // Main weather data info
                            item {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Box(modifier = Modifier) {
                                            BlurredWeatherIconBackground(painter = painter)
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                // Weather icon
                                                Image(
                                                    modifier = Modifier
                                                        .padding(top = 8.dp, start = 8.dp)
                                                        .size(72.dp)
                                                        .clip(RoundedCornerShape(12.dp)),
                                                    painter = painter,
                                                    contentDescription = "weather icon wth coil",
                                                    contentScale = ContentScale.Fit,
                                                )

                                                // Colum with city name country and weather state
                                                Column(
                                                    modifier = Modifier.padding(8.dp),
                                                    horizontalAlignment = Alignment.End,
                                                    verticalArrangement = Arrangement.Center
                                                ) {
                                                    Text(text = "$cityName, $country")
                                                    Text(
                                                        text = weather.mainWeather.toString(),
                                                        style = Typography.titleSmall,
                                                        fontWeight = FontWeight.ExtraBold
                                                    )
                                                }
                                            }

                                        }
                                        // Temperature row container
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.padding(start = 8.dp)) {
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
                                                        text = weather.temperature?.max?.toInt()
                                                            .toString(),
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = "|"
                                                    )
                                                    Text(
                                                        text = weather.temperature?.min?.toInt()
                                                            .toString()
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    // Hourly weather forecast content
                                    AnimatedVisibility(
                                        modifier = Modifier.padding(top = 12.dp),
                                        visible = !weather.hourlyWeather.isNullOrEmpty()
                                    ) {
                                        WeatherHourlyForecast(
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            theme = theme,
                                            darkTheme = darkTheme,
                                            hourlyWeatherList = weather.hourlyWeather!!
                                        )
                                    }
                                }
                            }

                            // Daily Forecast (5 days)
                            item {
                                AnimatedVisibility(visible = !weather.dailyWeather.isNullOrEmpty()) {
                                    // Forecast
                                    WeatherDailyForecast(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        theme = theme,
                                        darkTheme = darkTheme,
                                        dailyWeatherList = weather.dailyWeather!!
                                    )
                                }
                            }

                            // Weather sunrise/sunset
                            item {
                                val sunrise = remember {
                                    DateTimeUtils.formatMillisToTimeHoursMinutes(
                                        weather.timezone!!,
                                        weather.sunrise
                                    )
                                }

                                val sunset = remember {
                                    DateTimeUtils.formatMillisToTimeHoursMinutes(
                                        weather.timezone!!,
                                        weather.sunset
                                    )
                                }

                                WeatherSunriseSunset(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    theme = theme,
                                    darkTheme = darkTheme,
                                    sunrise = sunrise,
                                    sunset = sunset
                                )
                            }

                            // Weather environment
                            item {
                                WeatherEnvironment(
                                    theme = theme,
                                    darkTheme = darkTheme,
                                    cloudiness = weather.clouds,
                                    pressure = weather.pressure,
                                    humidity = weather.humidity,
                                    windSpeed = weather.windSpeed,
                                    windDegree = weather.windDegree
                                )
                            }

                            item {
                                WeatherDataProvidedBy(theme, darkTheme)
                            }
                        }
                    }
                }

                is WeatherUIState.Error -> Box(modifier = Modifier)
            }
        }
    }
}

@Composable
fun WeatherDataProvidedBy(theme: AppTheme, darkTheme: Boolean) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(
                space = 16.dp,
                alignment = Alignment.End
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.weather_data_provided_by),
                fontSize = 12.sp
            )
            Card {
                Box(modifier = Modifier.padding(8.dp), contentAlignment = Alignment.Center) {
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

///////////////////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////////////////
@DevicePreviews
@Composable
fun PreviewWeatherMoreData(@PreviewParameter(PreviewProviderWeather::class) weather: WeatherModel) {
    TheLabTheme(theme = AppTheme.Default) {
        WeatherMoreData(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            weather = weather
        )
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherRealFeels(@PreviewParameter(PreviewProviderWeather::class) weather: WeatherModel) {
    val sunrise = remember {
        DateTimeUtils.formatMillisToTimeHoursMinutes(
            weather.timezone!!,
            weather.sunrise
        )
    }

    val sunset = remember {
        DateTimeUtils.formatMillisToTimeHoursMinutes(
            weather.timezone!!,
            weather.sunset
        )
    }
    TheLabTheme(theme = AppTheme.Default) {
        WeatherSunriseSunset(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            sunrise = sunrise,
            sunset = sunset,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherEnvironmentItem(@PreviewParameter(PreviewProviderWeather::class) weather: WeatherModel) {
    TheLabTheme(theme = AppTheme.Default) {
        WeatherEnvironmentItem(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            cardTitleIcon = R.drawable.ic_wind,
            cardTitle = "Wind",
            content = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "32 km//H",
                        style = TextStyle(fontWeight = FontWeight.W700)
                    )

                    Icon(
                        modifier = Modifier.size(20.dp),
                        painter = painterResource(id = R.drawable.ic_wind_south),
                        contentDescription = "wind direction icon"
                    )
                }
            }
        )
    }
}

@DevicePreviews
@Composable
fun PreviewWeatherEnvironment(@PreviewParameter(PreviewProviderWeather::class) weather: WeatherModel) {
    TheLabTheme(theme = AppTheme.Default) {
        WeatherEnvironment(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            cloudiness = weather.clouds,
            pressure = weather.pressure,
            humidity = weather.humidity,
            windSpeed = weather.windSpeed,
            windDegree = weather.windDegree,
        )
    }
}

@DevicePreviews
@Composable
fun PreviewBlurredWeatherIconBackground(@PreviewParameter(PreviewProviderWeather::class) weather: WeatherModel) {
    TheLabTheme(theme = AppTheme.Default) {
        BlurredWeatherIconBackground(painter = painterResource(com.riders.thelab.core.ui.R.drawable.logo_colors))
    }
}

@DevicePreviews
@Composable
private fun PreviewWeatherMainCityContent(
    @PreviewParameter(PreviewProviderWeatherUIState::class) weatherUiState: WeatherUIState
) {
    TheLabTheme(theme = AppTheme.Default) {
        WeatherMainCityContent(
            theme = AppTheme.Default, darkTheme = isSystemInDarkTheme(),
            weatherUIState = weatherUiState,
            isWeatherMoreDataVisible = false
        ) {}
    }
}

@DevicePreviews
@Composable
private fun PreviewWeatherDataProvidedBy(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        WeatherDataProvidedBy(theme = appTheme, darkTheme = isSystemInDarkTheme())
    }
}