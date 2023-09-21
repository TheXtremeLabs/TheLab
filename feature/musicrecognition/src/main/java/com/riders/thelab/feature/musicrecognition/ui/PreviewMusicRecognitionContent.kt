package com.riders.thelab.feature.musicrecognition.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.musicrecognition.R


///////////////////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////////////////
@Composable
fun MusicRecognitionContent() {
    val context = LocalContext.current

    TheLabTheme {
        Scaffold(topBar = { TheLabTopAppBar {} }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically)
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxHeight(.25f)
                        .fillMaxWidth(.45f)
                        .weight(.3f),
                    painter = if (!isSystemInDarkTheme()) painterResource(id = R.drawable.acr_cloud_x_shazam) else painterResource(
                        id = R.drawable.acr_cloud_x_shazam_white
                    ),
                    contentDescription = "acr_cloud_shazam_icon"
                )

                Column(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(text = "Choose a music recognition provider")

                    Button(
                        modifier = Modifier
                            .fillMaxWidth(.6f),
                        onClick = {
                            // (context.findActivity() as MusicRecognitionChooserActivity).launchACRCloudActivity()
                        }
                    ) {
                        Text(text = "ACRCloud")

                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth(.6f),
                        onClick = {
                            //        (context.findActivity() as MusicRecognitionChooserActivity).launchShazamActivity()
                        },
                        enabled = false
                    ) {
                        Text(text = "Shazam")
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
fun PreviewMusicRecognitionContent() {
    TheLabTheme {
        MusicRecognitionContent()
    }
}