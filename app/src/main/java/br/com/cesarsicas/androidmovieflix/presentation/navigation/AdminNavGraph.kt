package br.com.cesarsicas.androidmovieflix.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable

fun NavGraphBuilder.adminGraph(navController: NavHostController) {
    composable(Routes.ADMIN_ENTRY) {
        PlaceholderScreen(navController = navController, title = "Admin (entry redirect)")
    }
    composable(Routes.ADMIN_LOGIN) {
        PlaceholderScreen(navController = navController, title = "Admin Login")
    }
    composable(Routes.ADMIN_HOME) {
        PlaceholderScreen(navController = navController, title = "Admin Home")
    }
    composable(Routes.ADMIN_WATCH_PARTY) {
        PlaceholderScreen(navController = navController, title = "Admin Watch Party")
    }
    composable(Routes.ADMIN_NEW_TRANSMISSION) {
        PlaceholderScreen(navController = navController, title = "New Transmission")
    }
    composable(Routes.ADMIN_UPLOAD_MOVIE) {
        PlaceholderScreen(navController = navController, title = "Upload Movie")
    }
}
