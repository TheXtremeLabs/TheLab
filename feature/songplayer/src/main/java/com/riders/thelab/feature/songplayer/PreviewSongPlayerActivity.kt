package com.riders.thelab.feature.songplayer

import androidx.compose.runtime.Composable
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.theme.TheLabTheme


///////////////////////////////////////
//
// COMPOSE
//
///////////////////////////////////////
@Composable
fun SongPlayerContent(){

    TheLabTheme {

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