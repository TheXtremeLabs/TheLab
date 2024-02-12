package com.riders.thelab.feature.theaters

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.compose.TMDBUiState
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.local.model.tmdb.TMDBVideoModel
import com.riders.thelab.core.data.local.model.tmdb.toModel
import com.riders.thelab.core.data.remote.dto.tmdb.MovieDto
import com.riders.thelab.core.data.remote.dto.tmdb.VideoDto


class PreviewProviderTMDBVideoModel : PreviewParameterProvider<TMDBVideoModel> {
    override val values: Sequence<TMDBVideoModel>
        get() = sequenceOf(
            VideoDto(
                iso_639_1 = "en",
                iso_3166_1 = "US",
                name = "20th Anniversary Trailer",
                key = "dfeUzm6KF4g",
                site = "YouTube",
                size = 1080,
                type = "Trailer",
                official = true,
                publishedAt = "2019-10-15T18:59:47.000Z",
                id = "64fb16fbdb4ed610343d72c3"
            ).toModel()
        )
}

class PreviewProviderTMDBItemModel : PreviewParameterProvider<TMDBItemModel> {
    override val values: Sequence<TMDBItemModel>
        get() = sequenceOf(
            MovieDto(
                0,
                "",
                false,
                "/efpojdpcjzidcjpzdko.jpg",
                emptySet(),
                "en-US",
                "Expend4bles",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
                50.6,
                "/fv45onsdvdv.jpg",
                "2023-10-25",
                false,
                7.6,
                3455
            ).toModel()
        )
}

class PreviewProviderTMDBDetailUiState : PreviewParameterProvider<TMDBUiState.TMDBDetailUiState> {
    override val values: Sequence<TMDBUiState.TMDBDetailUiState>
        get() = sequenceOf(
            TMDBUiState.TMDBDetailUiState.Error(""),
            TMDBUiState.TMDBDetailUiState.Success(
                MovieDto(
                    0,
                    "",
                    false,
                    "/efpojdpcjzidcjpzdko.jpg",
                    emptySet(),
                    "en-US",
                    "Expend4bles",
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
                    50.6,
                    "/fv45onsdvdv.jpg",
                    "2023-10-25",
                    false,
                    7.6,
                    3455
                ).toModel()
            ),
            TMDBUiState.TMDBDetailUiState.Loading
        )
}