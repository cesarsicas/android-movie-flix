package br.com.cesarsicas.androidmovieflix.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.cesarsicas.androidmovieflix.presentation.admin.AdminHomeScreen
import br.com.cesarsicas.androidmovieflix.presentation.admin.AdminLoginScreen

fun NavGraphBuilder.adminGraph(navController: NavHostController) {
    composable(Routes.ADMIN_ENTRY) {
        LaunchedEffect(Unit) {
            navController.navigate(Routes.ADMIN_HOME) {
                popUpTo(Routes.ADMIN_ENTRY) { inclusive = true }
            }
        }
    }
    composable(Routes.ADMIN_LOGIN) {
        AdminLoginScreen(navController = navController)
    }
    composable(Routes.ADMIN_HOME) {
        AdminHomeScreen(navController = navController)
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
