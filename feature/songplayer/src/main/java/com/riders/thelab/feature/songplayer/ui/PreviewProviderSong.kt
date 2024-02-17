package com.riders.thelab.feature.songplayer.ui

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.music.SongModel

class PreviewProviderSong : PreviewParameterProvider<SongModel> {
    override val values: Sequence<SongModel>
        get() = sequenceOf(
            SongModel(
                -1,
                "Danger",
                "/somepath/subdirectory/Danger.mp3",
                "/somepath/subdirectory/drawableUri",
                false
            ),
            SongModel(
                0,
                "House",
                "/somepath/subdirectory/House.mp3",
                "/somepath/subdirectory/drawableUri",
                false
            ),
            SongModel(
                1,
                "Danger II",
                "/somepath/subdirectory/Danger_2.mp3",
                "/somepath/subdirectory/drawableUri",
                false
            ),
            SongModel(
                2,
                "Danger III",
                "/somepath/subdirectory/Danger_3.mp3",
                "/somepath/subdirectory/drawableUri",
                true
            )
        )
}

class PreviewProviderSongList : PreviewParameterProvider<List<SongModel>> {
    override val values: Sequence<List<SongModel>>
        get() = sequenceOf(
            listOf(
                SongModel(
                    -1,
                    "Danger",
                    "/somepath/subdirectory/Danger.mp3",
                    "/somepath/subdirectory/drawableUri",
                    false
                ),
                SongModel(
                    0,
                    "House",
                    "/somepath/subdirectory/House.mp3",
                    "/somepath/subdirectory/drawableUri",
                    false
                ),
                SongModel(
                    1,
                    "Danger II",
                    "/somepath/subdirectory/Danger_2.mp3",
                    "/somepath/subdirectory/drawableUri",
                    false
                ),
                SongModel(
                    2,
                    "Danger III",
                    "/somepath/subdirectory/Danger_3.mp3",
                    "/somepath/subdirectory/drawableUri",
                    false
                )
            )
        )
}