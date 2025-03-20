package com.riders.thelab.feature.kat.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.riders.thelab.core.ui.compose.annotation.DevicePreviews
import com.riders.thelab.core.ui.compose.data.AppTheme
import com.riders.thelab.core.ui.compose.previewprovider.AppThemePreviewProvider
import com.riders.thelab.core.ui.compose.theme.TheLabTheme
import com.riders.thelab.feature.kat.data.local.compose.KatScreenRoute


///////////////////////////////
//
// COMPOSE
//
///////////////////////////////
@DevicePreviews
@Composable
fun UserIcon(
    modifier: Modifier = Modifier.size(40.dp),
    card: Color = MaterialTheme.colorScheme.primaryContainer,
    iconColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    Card(
        modifier = modifier,
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = card)
//                    colors = CardDefaults.cardColors(containerColor = if (!isSystemInDarkTheme()) md_theme_dark_primaryContainer else md_theme_light_primaryContainer)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Filled.Person, contentDescription = null, tint = iconColor)
        }
    }
}

@Composable
fun KatHeader(theme: AppTheme, darkTheme: Boolean, modelName: String, userEmail: String) {
    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Phone
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Filled.PhoneAndroid, contentDescription = "phone_icon")
                    Text(text = modelName)
                }


                // User Email
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.AlternateEmail,
                        contentDescription = "phone_icon"
                    )
                    Text(text = userEmail, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

@Composable
fun KatNavHost(
    theme: AppTheme, darkTheme: Boolean,
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String = KatScreenRoute.Chat.route,
    viewModel: KatMainViewModel,
    profileViewModel: KatProfileViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Down) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Up) },
    ) {
        composable(KatScreenRoute.Chat.route) {
            KatChatScreen(
                theme = theme,
                darkTheme = darkTheme,
                viewModel.chatRooms
            )
        }
        composable(KatScreenRoute.Profile.route) {
            KatUserScreen(
                theme = theme,
                darkTheme = darkTheme,
                profileViewModel
            )
        }
    }
}

@Composable
fun KatMainContent(
    theme: AppTheme, darkTheme: Boolean,
    viewModel: KatMainViewModel,
    profileViewModel: KatProfileViewModel
) {

    val navController: NavHostController = rememberNavController()

    val items = listOf(
        KatScreenRoute.Chat,
        KatScreenRoute.Profile
    )

    TheLabTheme(theme = theme, darkTheme = darkTheme) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                BottomNavigation(backgroundColor = MaterialTheme.colorScheme.background) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    items.forEach { screen ->

                        val textAndIconColor: Color =
                            if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                if (!isSystemInDarkTheme()) Color.DarkGray else Color.LightGray
                            }

                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    if (screen is KatScreenRoute.Chat) Icons.AutoMirrored.Filled.Message else Icons.Filled.Person,
                                    contentDescription = null,
                                    tint = textAndIconColor
                                )
                            },
                            label = {
                                Text(
                                    stringResource(screen.resourceId),
                                    style = TextStyle(color = textAndIconColor)
                                )
                            },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            },
                            selectedContentColor = MaterialTheme.colorScheme.primary,
                            unselectedContentColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                LocalAbsoluteTonalElevation.current
                            )
                        )
                    }
                }
            }
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    KatHeader(
                        theme = theme,
                        darkTheme = darkTheme,
                        viewModel.modelName,
                        viewModel.userEmail
                    )
                }
                Box(modifier = Modifier.fillMaxWidth()) {
                    KatNavHost(
                        theme = theme, darkTheme = darkTheme,
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        viewModel = viewModel,
                        profileViewModel = profileViewModel
                    )
                }
            }
        }
    }
}


///////////////////////////////
//
// PREVIEWS
//
///////////////////////////////
@DevicePreviews
@Composable
private fun PreviewKatHeader(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    TheLabTheme(theme = appTheme) {
        KatHeader(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            modelName = "Zebra TC58",
            userEmail = "test@test.fr"
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewKatNavHost(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    val viewModel: KatMainViewModel = hiltViewModel()
    val profileViewModel: KatProfileViewModel = hiltViewModel()
    val navController = rememberNavController()

    TheLabTheme(theme = appTheme) {
        KatNavHost(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            viewModel = viewModel,
            profileViewModel = profileViewModel,
            modifier = Modifier.fillMaxSize(),
            navController = navController
        )
    }
}

@DevicePreviews
@Composable
private fun PreviewKatMainContent(@PreviewParameter(AppThemePreviewProvider::class) appTheme: AppTheme) {
    val viewModel: KatMainViewModel = hiltViewModel()
    val profileViewModel: KatProfileViewModel = hiltViewModel()

    TheLabTheme(theme = appTheme) {
        KatMainContent(
            theme = appTheme,
            darkTheme = isSystemInDarkTheme(),
            viewModel = viewModel,
            profileViewModel = profileViewModel
        )
    }
}