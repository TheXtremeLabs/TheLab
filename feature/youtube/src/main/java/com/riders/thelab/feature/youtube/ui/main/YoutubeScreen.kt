package com.riders.thelab.feature.youtube.ui.main

import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString

sealed class YoutubeScreen(val route: NotBlankString) {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    data object List : YoutubeScreen(NotBlankString.create("list"))

    @OptIn(ExperimentalKotoolsTypesApi::class)
    data object Detail : YoutubeScreen(NotBlankString.create("details/{id}"))
}