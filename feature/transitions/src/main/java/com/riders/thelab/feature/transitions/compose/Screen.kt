package com.riders.thelab.feature.transitions.compose

import org.kotools.types.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import kotools.types.text.toNotBlankString

sealed class Screen(val route: NotBlankString) {

    @OptIn(ExperimentalKotoolsTypesApi::class)
    data object Main : Screen("main".toNotBlankString().getOrThrow())

    @OptIn(ExperimentalKotoolsTypesApi::class)
    data object Detail : Screen("detail".toNotBlankString().getOrThrow())
}