package com.riders.thelab.ui.theaters

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.compose.annotation.DevicePreviews
import com.riders.thelab.core.compose.ui.theme.TheLabTheme
import com.riders.thelab.core.compose.utils.getAsyncImagePainter
import com.riders.thelab.data.local.bean.MovieEnum
import com.riders.thelab.data.local.model.Members
import com.riders.thelab.data.local.model.Movie
import com.riders.thelab.data.local.model.Scenarist
import timber.log.Timber
import java.util.Locale


@Composable
fun MemberItem(member: Members) {
    val painter = getAsyncImagePainter(
        context = LocalContext.current,
        dataUrl = member.urlThumbnail
    )

    Column(
        modifier = Modifier.width(96.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Card(modifier = Modifier.size(72.dp), shape = CircleShape) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painter,
                contentDescription = "Scenarist thumbnail",
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = member.firstName,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = member.lastName.uppercase(Locale.getDefault()),
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Center
            )
        }
    }
}


@Composable
fun Director(movie: Movie) {
    if (null == movie.directors) {
        Timber.e("Unable to get Director thumbnail url")
        return
    }

    val painter = getAsyncImagePainter(
        context = LocalContext.current,
        dataUrl = movie.directors!![0].urlThumbnail
    )

    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Director".uppercase(Locale.getDefault()),
                style = TextStyle(fontWeight = FontWeight.W700)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(modifier = Modifier.size(72.dp), shape = CircleShape) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painter,
                        contentDescription = "Director thumbnail",
                        contentScale = ContentScale.FillWidth
                    )
                }

                Text(text = "${movie.directors!![0].firstName} ${movie.directors!![0].lastName}")
            }
        }
    }
}

@Composable
fun Scenarists(movie: Movie) {
    val lazyRowListState = rememberLazyListState()

    if (null == movie.scenarists) {
        Timber.e("Unable to get Scenarists thumbnail url")
        return
    }

    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Scenarists".uppercase(Locale.getDefault()),
                style = TextStyle(fontWeight = FontWeight.W700)
            )

            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = lazyRowListState
            ) {
                items(items = movie.scenarists!!) {

                    MemberItem(member = it)
                }
            }
        }
    }
}

@Composable
fun Casting(movie: Movie) {
    val lazyRowListState = rememberLazyListState()

    if (null == movie.cast) {
        Timber.e("Unable to get Casting thumbnail url")
        return
    }

    TheLabTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Cast Members".uppercase(Locale.getDefault()),
                style = TextStyle(fontWeight = FontWeight.W700)
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                state = lazyRowListState
            ) {
                items(items = movie.cast!!) {
                    MemberItem(member = it)
                }
            }
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
fun PreviewMemberItem() {
    val member: Members = Scenarist(
        "Di Caprio",
        "Leonardo",
        "https://wwww.google.com"
    )
    TheLabTheme {
        MemberItem(member = member)
    }
}

@DevicePreviews
@Composable
fun PreviewDirector() {
    val movie = MovieEnum.GUARDIANS_OF_THE_GALAXY.toMovie()
    TheLabTheme {
        Director(movie = movie)
    }
}

@DevicePreviews
@Composable
fun PreviewScenarists() {
    val movie = MovieEnum.GUARDIANS_OF_THE_GALAXY.toMovie()
    TheLabTheme {
        Scenarists(movie = movie)
    }
}

@DevicePreviews
@Composable
fun PreviewCasting() {
    val movie = MovieEnum.GUARDIANS_OF_THE_GALAXY.toMovie()
    TheLabTheme {
        Casting(movie = movie)
    }
}