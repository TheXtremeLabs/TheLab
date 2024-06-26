package com.riders.thelab.feature.youtube.ui.main

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.YoutubeUiState
import com.riders.thelab.core.data.local.model.youtube.Video
import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

class PreviewProviderYoutube : PreviewParameterProvider<YoutubeUiState> {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    override val values: Sequence<YoutubeUiState>
        get() = sequenceOf(
            YoutubeUiState.Loading,
            YoutubeUiState.Error(NotBlankString.create("Error occurred while getting value")),
            YoutubeUiState.Success(
                listOf(
                    Video(
                        id = "YQHsXMglC9A",
                        name = "Adele - Hello",
                        description = "‘Hello' is taken from the new album, 25, out November 20. http://adele.com \n Available now from iTunes http://smarturl.it/itunes25 \n Available now from Amazon http://smarturl.it/25amazon ",
                        imageUrl = "https://i.ytimg.com/vi/YQHsXMglC9A/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=YQHsXMglC9A"
                    ),
                    Video(
                        id = "1wYNFfgrXTI",
                        name = "Eminem - When I'm Gone",
                        description = "Music video by Eminem performing When I'm Gone. (C) 2005 Aftermath Entertainment/Interscope Records",
                        imageUrl = "https://i.ytimg.com/vi/1wYNFfgrXTI/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=1wYNFfgrXTI"
                    ),
                    Video(
                        id = "_Yhyp-_hX2s",
                        name = "Eminem - Lose Yourself [HD]",
                        description = "feat. Eminem from the movie 8 MILE",
                        imageUrl = "https://i.ytimg.com/vi/_Yhyp-_hX2s/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=_Yhyp-_hX2s"
                    ),
                    Video(
                        id = "D4hAVemuQXY",
                        name = "Eminem - Sing For The Moment",
                        description = "Music video by Eminem performing Sing For The Moment. (C) 2002 Aftermath Records PLUS",
                        imageUrl = "https://i.ytimg.com/vi/D4hAVemuQXY/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=D4hAVemuQXY"
                    ),
                    Video(
                        id = "hLQl3WQQoQ0",
                        name = "Adele - Someone Like You",
                        description = "Music video by Adele performing Someone Like You. (C) 2011 XL Recordings LtdPLUS",
                        imageUrl = "https://i.ytimg.com/vi/hLQl3WQQoQ0/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=hLQl3WQQoQ0"
                    ),
                    Video(
                        id = "fWNaR-rxAic",
                        name = "Carly Rae Jepsen - Call Me Maybe",
                        description = "Get E•MO•TION on iTunes now: http://smarturl.it/E-MO-TION\nSign up for Carly Rae Jepsen news here: http://smarturl.it/CRJ.News",
                        imageUrl = "https://i.ytimg.com/vi/fWNaR-rxAic/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=fWNaR-rxAic"
                    ),
                    Video(
                        id = "OpQFFLBMEPI",
                        name = "P!nk - Just Give Me A Reason ft. Nate Ruess",
                        description = "P!nk's official music video for 'Just Give Me A Reason' ft. Nate Ruess. Click to listen to P!nk on Spotify: http://smarturl.it/PSpot?IQid=PJGMR",
                        imageUrl = "https://i.ytimg.com/vi/OpQFFLBMEPI/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=OpQFFLBMEPI"
                    ),
                    Video(
                        id = "2vjPBrBU-TM",
                        name = "Sia - Chandelier (Official Video)",
                        description = "Sia's official music video for 'Chandelier'. Click to listen to Sia on Spotify: http://smarturl.it/SSpot?IQid=SiaC",
                        imageUrl = "https://i.ytimg.com/vi/2vjPBrBU-TM/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=2vjPBrBU-TM"
                    ),
                    Video(
                        id = "lWA2pjMjpBs",
                        name = "Rihanna - Diamonds",
                        description = "Get Rihanna’s eighth studio album ANTI now:\nDownload on TIDAL: http://smarturl.it/downloadANTI\nStream on TIDAL: http://smarturl.it/streamANTIdlx",
                        imageUrl = "https://i.ytimg.com/vi/lWA2pjMjpBs/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=lWA2pjMjpBs"
                    ),
                    Video(
                        id = "bnVUHWCynig",
                        name = "Beyoncé - Halo",
                        description = "Remember those walls I built? Well, baby they're tumbling down...",
                        imageUrl = "https://i.ytimg.com/vi/bnVUHWCynig/mqdefault.jpg",
                        videoUrl = "https://www.youtube.com/watch?v=bnVUHWCynig"
                    )
                )
            )
        )
}

class PreviewProviderVideo : PreviewParameterProvider<Video> {
    override val values: Sequence<Video>
        get() = sequenceOf(
            Video(
                id = "YQHsXMglC9A",
                name = "Adele - Hello",
                description = "‘Hello' is taken from the new album, 25, out November 20. http://adele.com \n Available now from iTunes http://smarturl.it/itunes25 \n Available now from Amazon http://smarturl.it/25amazon ",
                imageUrl = "https://i.ytimg.com/vi/YQHsXMglC9A/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=YQHsXMglC9A"
            ),
            Video(
                id = "1wYNFfgrXTI",
                name = "Eminem - When I'm Gone",
                description = "Music video by Eminem performing When I'm Gone. (C) 2005 Aftermath Entertainment/Interscope Records",
                imageUrl = "https://i.ytimg.com/vi/1wYNFfgrXTI/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=1wYNFfgrXTI"
            ),
            Video(
                id = "_Yhyp-_hX2s",
                name = "Eminem - Lose Yourself [HD]",
                description = "feat. Eminem from the movie 8 MILE",
                imageUrl = "https://i.ytimg.com/vi/_Yhyp-_hX2s/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=_Yhyp-_hX2s"
            ),
            Video(
                id = "D4hAVemuQXY",
                name = "Eminem - Sing For The Moment",
                description = "Music video by Eminem performing Sing For The Moment. (C) 2002 Aftermath Records PLUS",
                imageUrl = "https://i.ytimg.com/vi/D4hAVemuQXY/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=D4hAVemuQXY"
            ),
            Video(
                id = "hLQl3WQQoQ0",
                name = "Adele - Someone Like You",
                description = "Music video by Adele performing Someone Like You. (C) 2011 XL Recordings LtdPLUS",
                imageUrl = "https://i.ytimg.com/vi/hLQl3WQQoQ0/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=hLQl3WQQoQ0"
            ),
            Video(
                id = "fWNaR-rxAic",
                name = "Carly Rae Jepsen - Call Me Maybe",
                description = "Get E•MO•TION on iTunes now: http://smarturl.it/E-MO-TION\nSign up for Carly Rae Jepsen news here: http://smarturl.it/CRJ.News",
                imageUrl = "https://i.ytimg.com/vi/fWNaR-rxAic/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=fWNaR-rxAic"
            ),
            Video(
                id = "OpQFFLBMEPI",
                name = "P!nk - Just Give Me A Reason ft. Nate Ruess",
                description = "P!nk's official music video for 'Just Give Me A Reason' ft. Nate Ruess. Click to listen to P!nk on Spotify: http://smarturl.it/PSpot?IQid=PJGMR",
                imageUrl = "https://i.ytimg.com/vi/OpQFFLBMEPI/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=OpQFFLBMEPI"
            ),
            Video(
                id = "2vjPBrBU-TM",
                name = "Sia - Chandelier (Official Video)",
                description = "Sia's official music video for 'Chandelier'. Click to listen to Sia on Spotify: http://smarturl.it/SSpot?IQid=SiaC",
                imageUrl = "https://i.ytimg.com/vi/2vjPBrBU-TM/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=2vjPBrBU-TM"
            ),
            Video(
                id = "lWA2pjMjpBs",
                name = "Rihanna - Diamonds",
                description = "Get Rihanna’s eighth studio album ANTI now:\nDownload on TIDAL: http://smarturl.it/downloadANTI\nStream on TIDAL: http://smarturl.it/streamANTIdlx",
                imageUrl = "https://i.ytimg.com/vi/lWA2pjMjpBs/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=lWA2pjMjpBs"
            ),
            Video(
                id = "bnVUHWCynig",
                name = "Beyoncé - Halo",
                description = "Remember those walls I built? Well, baby they're tumbling down...",
                imageUrl = "https://i.ytimg.com/vi/bnVUHWCynig/mqdefault.jpg",
                videoUrl = "https://www.youtube.com/watch?v=bnVUHWCynig"
            )
        )
}
class PreviewProviderVideos : PreviewParameterProvider<List<Video>> {
    override val values: Sequence<List<Video>>
        get() = sequenceOf(PreviewProviderVideo().values.toList())
}
