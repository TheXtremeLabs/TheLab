package com.riders.thelab.feature.songplayer

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.component.TheLabTopAppBar
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun SongPlayerContent(){

    TheLabTheme {
        Scaffold(
            topBar = {
                TheLabTopAppBar(
                    title = stringResource(id = com.riders.thelab.core.ui.R.string.activity_title_music),
                    navigationIcon = {})
            }
        ) { contentPadding ->

        }
    }
}



///////////////////////////////////////
//
// PREVIEWS
//
///////////////////////////////////////
@DevicePreviews
@Composable
private fun PreviewSongPlayerContent(){

    TheLabTheme {
        SongPlayerContent()
    }
}