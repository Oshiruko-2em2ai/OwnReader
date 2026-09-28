package com.ownreader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ownreader.ui.screens.home.HomeScreen
import com.ownreader.ui.screens.reader.ReaderScreen
import com.ownreader.ui.screens.settings.SettingsScreen

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
        composable(
            route = NavigationRoute.Reader.route,
            arguments = listOf(
                navArgument("comicTitle") { type = NavType.StringType },
                navArgument("folderUri") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("comicTitle") ?: "Comic"
            val folderUri = backStackEntry.arguments?.getString("folderUri") ?: ""
            ReaderScreen(
                title = title,
                folderUri = folderUri,
                navController = navController
            )
        }
        composable(NavigationRoute.Settings.route) {
            SettingsScreen(navController = navController)
        }
    }
}

sealed class NavigationRoute(val route: String) {
    object Home : NavigationRoute("home")
    object Library : NavigationRoute("library")
    object Reader : NavigationRoute("reader/{comicTitle}/{folderUri}")
    object Settings : NavigationRoute("settings")
}
