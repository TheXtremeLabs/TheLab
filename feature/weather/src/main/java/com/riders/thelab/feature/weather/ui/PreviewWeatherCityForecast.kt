package com.riders.thelab.feature.weather.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import com.riders.thelab.core.common.utils.DateTimeUtils
import com.riders.thelab.core.domain.model.weather.Weather
import com.riders.thelab.core.ui.R
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.weather.ui.previewprovider.PreviewProviderWeather


///////////////////////////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////////////////////////
@Composable
fun WeatherDailyForecast(
    theme: AppTheme,
    darkTheme: Boolean,
    modifier: Modifier = Modifier,
    dailyWeatherList: List<Weather>
) {
    val listState = rememberLazyListState()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .then(modifier)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(8.dp),
                    text = "Weather trends for the next 5 days"
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 1.dp)
                        .heightIn(max = 360.dp)
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
                                .data(dailyWeather.weatherIconUrl.toString())
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
                                text = DateTimeUtils.getDayFromTime(dailyWeather.dateTimeUTC),
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
                                    text = "${dailyWeather.temperature?.max?.toInt()}°",
                                    fontWeight = FontWeight.Bold
                                )
                                // current temperature
                                Text(
                                    text = "${dailyWeather.temperature?.min?.toInt()}°"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherHourlyForecast(
    theme: AppTheme,
    darkTheme: Boolean,
    hourlyWeatherList: List<Weather>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(modifier = Modifier.then(modifier)) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Hourly weather"
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(56.dp, 100.dp),
                    state = listState,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items(items = hourlyWeatherList) { hourlyWeather ->
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest
                                .Builder(LocalContext.current)
                                .data(hourlyWeather.weatherIconUrl.toString())
                                .apply {
                                    crossfade(true)
                                    allowHardware(false)
                                }
                                .build(),
                            placeholder = painterResource(R.drawable.logo_colors),
                        )

                        Column(
                            modifier = Modifier.fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // current temperature
                            Text(
                                text = DateTimeUtils.formatMillisToTimeHoursMinutes(hourlyWeather.dateTimeUTC),
                                fontWeight = FontWeight.Bold
                            )

                            WeatherIcon(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                weatherIconUrl = hourlyWeather.weatherIconUrl.toString()
                            )

                            // current temperature
                            Text(
                                text = "${hourlyWeather.temperature?.temperature?.toInt()}°"
                            )
                        }
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
private fun PreviewWeatherDailyForecast(@PreviewParameter(PreviewProviderWeather::class) weather: Weather) {
    TheLabTheme(theme = AppTheme.Default) {
        WeatherDailyForecast(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier.fillMaxWidth(),
            dailyWeatherList = weather.dailyWeather!!
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewWeatherHourlyForecast(@PreviewParameter(PreviewProviderWeather::class) weather: Weather) {
    TheLabTheme(theme = AppTheme.Default) {
        WeatherHourlyForecast(
            theme = AppTheme.Default,
            darkTheme = isSystemInDarkTheme(),
            modifier = Modifier.fillMaxWidth(),
            hourlyWeatherList = weather.hourlyWeather!!
        )
    }
}