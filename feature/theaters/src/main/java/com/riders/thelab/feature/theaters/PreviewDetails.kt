package com.riders.thelab.feature.theaters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.data.local.bean.MovieEnum
import com.riders.thelab.core.data.local.model.Movie
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import java.util.Locale
import kotlin.math.roundToInt


///////////////////////////////
//
// COMPOSABLE
//
///////////////////////////////
@Composable
fun PopularityAndRating(movie: Movie) {
    TheLabTheme {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(modifier = Modifier) {
                    repeat(movie.rating.roundToInt()) {
                        Icon(
                            imageVector = Icons.Filled.StarRate,
                            contentDescription = "Star rating icon"
                        )
                    }
                    repeat(5 - movie.rating.roundToInt()) {
                        Icon(
                            imageVector = Icons.Outlined.StarRate,
                            contentDescription = "outlined Star rating icon"
                        )
                    }
                }

                Text(text = "${movie.rating}/5")
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(text = "${movie.popularity}".uppercase())
                Text(text = "Popularity", fontWeight = FontWeight.W700)
            }
        }
    }
}

@Composable
fun Overview(movie: Movie) {
    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier,
                text = "Overview".uppercase(Locale.getDefault()),
                style = TextStyle(fontWeight = FontWeight.W700)
            )
            Text(modifier = Modifier.fillMaxWidth(), text = movie.overview)
        }
    }
}

@Composable
fun PopularityAndRating(tmdbItem: TMDBItemModel) {
    TheLabTheme {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(modifier = Modifier) {
                    repeat(tmdbItem.rating.roundToInt()) {
                        Icon(
                            imageVector = Icons.Filled.StarRate,
                            contentDescription = "Star rating icon"
                        )
                    }
                    repeat(5 - tmdbItem.rating.roundToInt()) {
                        Icon(
                            imageVector = Icons.Outlined.StarRate,
                            contentDescription = "outlined Star rating icon"
                        )
                    }
                }

                Text(text = "${tmdbItem.rating.roundToInt()}/10")
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text(text = "${tmdbItem.popularity}".uppercase())
                Text(text = "Popularity", fontWeight = FontWeight.W700)
            }
        }
    }
}

@Composable
fun Overview(tmdbItem: TMDBItemModel) {
    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier,
                text = "Overview".uppercase(Locale.getDefault()),
                style = TextStyle(fontWeight = FontWeight.W700)
            )
            Text(modifier = Modifier.fillMaxWidth(), text = tmdbItem.overview)
        }
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewPopularityAndRating() {
    val movie = MovieEnum.GUARDIANS_OF_THE_GALAXY.toMovie()
    TheLabTheme {
        PopularityAndRating(movie = movie)
    }
}

@DevicePreviews
@Composable
private fun PreviewOverview() {
    val movie = MovieEnum.GUARDIANS_OF_THE_GALAXY.toMovie()
    TheLabTheme {
        Overview(movie = movie)
    }
}

@DevicePreviews
@Composable
private fun PreviewTMDBPopularityAndRating() {

    TheLabTheme {
        PopularityAndRating(tmdbItem = TMDBItemModel.mockItem)
    }
}

@DevicePreviews
@Composable
private fun PreviewTMDBOverview() {
    TheLabTheme {
        Overview(tmdbItem = TMDBItemModel.mockItem)
    }
}