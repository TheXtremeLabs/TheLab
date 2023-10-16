package com.riders.thelab.ui.mainactivity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.riders.thelab.R
import com.riders.thelab.core.data.local.model.app.App
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.core.ui.compose.utils.findActivity
import com.riders.thelab.core.ui.utils.UIManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsNew(item: App) {

    val context = LocalContext.current

    /* Convert our Image Resource into a Bitmap */
    val bitmap = remember {
        UIManager.drawableToBitmap(item.appDrawableIcon!!)
    }
    /* Create the Palette, pass the bitmap to it */
    val palette = remember {
        bitmap.let { Palette.from(it).generate() }
    }

    /* Get the dark vibrant swatch */
    val darkVibrantSwatch = palette.darkVibrantSwatch

    TheLabTheme {
        Card(
            modifier = Modifier
                .width(width = dimensionResource(id = R.dimen.max_card_image_width))
                .height(height = dimensionResource(id = R.dimen.max_card_image_height)),
            onClick = { (context.findActivity() as MainActivity).launchApp(item) },
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2F)
                        .background(
                            when (item.appTitle) {
                                stringResource(id = R.string.activity_title_colors),
                                stringResource(id = R.string.activity_title_palette),
                                stringResource(id = R.string.activity_title_youtube_like),
                                stringResource(id = R.string.activity_title_google_drive),
                                stringResource(id = R.string.activity_title_google_sign_in),
                                stringResource(id = R.string.activity_title_weather),
                                stringResource(id = R.string.activity_title_compose),
                                stringResource(id = R.string.activity_title_lottie) -> {
                                    darkVibrantSwatch?.rgb?.let {
                                        Color(
                                            it
                                        )
                                    } ?: Color.Transparent
                                }

                                else -> {
                                    Color.Transparent
                                }
                            })
                ) {
                    Image(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxHeight()
                            .padding(32.dp),
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "app_icon",
                        contentScale = ContentScale.FillHeight
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1F)
                        .padding(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = if (item.appName != null) item.appName!! else item.appTitle!!
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = if (item.appDescription != null) item.appDescription!! else ""
                    )
                }
            }
        }
    }
}

@Composable
fun WhatsNewList(viewModel: MainActivityViewModel) {
    TheLabTheme {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(viewModel.whatsNewAppList.value) { whatsNewItem ->
                WhatsNew(item = whatsNewItem)
            }
        }
    }
}
