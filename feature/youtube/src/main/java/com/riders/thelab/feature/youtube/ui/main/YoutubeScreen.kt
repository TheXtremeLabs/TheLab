package com.riders.thelab.feature.youtube.ui.main

import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString
import org.kotools.types.ExperimentalKotoolsTypesApi

sealed class YoutubeScreen(val route: NotBlankString) {
    @OptIn(ExperimentalKotoolsTypesApi::class)
    data object List : YoutubeScreen("list".toNotBlankString().getOrThrow())

    @OptIn(ExperimentalKotoolsTypesApi::class)
    data object Detail : YoutubeScreen("details/{id}".toNotBlankString().getOrThrow())
}