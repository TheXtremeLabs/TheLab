package com.riders.thelab.feature.transitions.compose

import kotools.types.experimental.ExperimentalKotoolsTypesApi
import kotools.types.text.NotBlankString
import  java.io.Serializable

sealed class Screen(val route: NotBlankString) {

    @OptIn(ExperimentalKotoolsTypesApi::class)
    data object Main : Screen(NotBlankString.create("main"))

    @OptIn(ExperimentalKotoolsTypesApi::class)
    data object Detail : Screen(NotBlankString.create("detail"))
}