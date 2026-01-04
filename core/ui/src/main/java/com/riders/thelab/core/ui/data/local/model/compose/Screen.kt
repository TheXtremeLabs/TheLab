package com.riders.thelab.core.ui.data.local.model.compose

import androidx.navigation.NavDestination
import kotlinx.serialization.Serializable

@Serializable
abstract class Screen(val route: String)