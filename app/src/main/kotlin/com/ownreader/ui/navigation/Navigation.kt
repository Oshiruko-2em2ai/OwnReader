package com.ownreader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ownreader.ui.screens.home.HomeScreen

@Composable
fun OwnReaderNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Home.route
    ) {
        composable(NavigationRoute.Home.route) {
            HomeScreen(navController = navController)
        }
    }
}

sealed class NavigationRoute(val route: String) {
    object Home : NavigationRoute("home")
    object Library : NavigationRoute("library")
    object ComicReader : NavigationRoute("reader/{comicId}")
    object Settings : NavigationRoute("settings")
}
