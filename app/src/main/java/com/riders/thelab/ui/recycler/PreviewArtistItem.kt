package com.riders.thelab.ui.recycler

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.size.Scale
import coil.size.Size
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.music.ArtistModel
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.compose.utils.getCoilAsyncImagePainter
import com.riders.thelab.core.ui.utils.loadImage
import kotlinx.coroutines.launch
import timber.log.Timber


@DevicePreviews
@Composable
fun Artist(@PreviewParameter(PreviewProviderArtistModel::class) artist: ArtistModel) {

    ////////////////////////////////
    // Compose
    ////////////////////////////////
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedIndex by remember { mutableStateOf((-1).toByte()) }

    ////////////////////////////////
    // Coil
    ////////////////////////////////
    val painter = getCoilAsyncImagePainter(
        context = context,
        dataUrl = artist.urlThumb,
        size = Size.ORIGINAL,
        scale = Scale.FILL
    )
    val state = painter.state


    ////////////////////////////////
    // Composable
    ////////////////////////////////
    TheLabTheme {
        Card(
            modifier = Modifier
                .height(dimensionResource(id = R.dimen.max_card_image_height))
                .fillMaxWidth()
                .selectable(
                    selected = artist.id == selectedIndex,
                    onClick = {
                        selectedIndex =
                            if (selectedIndex != artist.id) artist.id else -1

                        (context.findActivity() as RecyclerViewActivity).onDetailClick(artist)
                    })
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)),
                    painter = painter,
                    contentDescription = "palette image wth coil",
                    contentScale = ContentScale.Crop,
                )

                when (state) {
                    is AsyncImagePainter.State.Loading -> {
                        Timber.i("state is AsyncImagePainter.State.Loading")
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .scale(0.5f)
                                .align(Alignment.CenterHorizontally)
                        )
                    }

                    is AsyncImagePainter.State.Success -> {
                        Timber.d("state is AsyncImagePainter.State.Success")
                        LaunchedEffect(key1 = painter) {
                            scope.launch { val image = painter.loadImage() }
                        }
                    }

                    is AsyncImagePainter.State.Error -> {
                        Timber.e("state is AsyncImagePainter.State.Error | ${state.result}")
                    }

                    else -> {
                        Timber.e("else branch")
                    }
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    text = artist.sceneName
                )
            }
        }
    }
}