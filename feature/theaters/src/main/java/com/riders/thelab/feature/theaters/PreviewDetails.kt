package com.riders.thelab.feature.theaters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.ui.R
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
fun Titles(title: String, originalTitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        )
        Text(
            text = originalTitle,
            style = TextStyle(
                color = Color.LightGray,
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp
            )
        )
    }

}

@Composable
fun PopularityAndRating(tmdbItem: TMDBItemModel) {

    val iconSize: Dp = 14.dp
    val rating = tmdbItem.rating.roundToInt()

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(modifier = Modifier) {
                repeat(rating) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Filled.StarRate,
                        contentDescription = "Star rating icon"
                    )
                }
                repeat(10 - rating) {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        imageVector = Icons.Outlined.StarRate,
                        contentDescription = "outlined Star rating icon"
                    )
                }
            }

            Text(text = "$rating / 10")
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

@Composable
fun Trailer(onTrailerButtonClicked: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = onTrailerButtonClicked) {
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                imageVector = Icons.Outlined.PlayCircleOutline,
                contentDescription = "Play trailer icon"
            )

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.watch_trailer),
                style = TextStyle(fontWeight = FontWeight.W700)
            )
        }
    }

}

@Composable
fun Overview(tmdbItem: TMDBItemModel) {

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


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewTMDBTitles(@PreviewParameter(PreviewProviderTMDBItemModel::class) item: TMDBItemModel) {
    TheLabTheme(true) {
        Titles(item.title, item.originalTitle)
    }
}

@DevicePreviews
@Composable
private fun PreviewTMDBPopularityAndRating(@PreviewParameter(PreviewProviderTMDBItemModel::class) item: TMDBItemModel) {
    TheLabTheme {
        PopularityAndRating(tmdbItem = item)
    }
}

@DevicePreviews
@Composable
private fun PreviewTMDBTrailer() {
    TheLabTheme {
        Trailer {}
    }
}

@DevicePreviews
@Composable
private fun PreviewTMDBOverview(@PreviewParameter(PreviewProviderTMDBItemModel::class) item: TMDBItemModel) {
    TheLabTheme {
        Overview(tmdbItem = item)
    }
}