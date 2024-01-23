package com.riders.thelab.feature.theaters

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.riders.thelab.core.data.local.model.tmdb.TMDBItemModel
import com.riders.thelab.core.data.local.model.tmdb.toModel
import com.riders.thelab.core.data.remote.dto.tmdb.MovieDto

class PreviewProvider : PreviewParameterProvider<TMDBItemModel> {

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
                50.56,
                3455
            ).toModel()
        )
}