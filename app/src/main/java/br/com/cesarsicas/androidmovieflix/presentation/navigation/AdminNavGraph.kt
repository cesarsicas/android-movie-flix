package br.com.cesarsicas.androidmovieflix.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import br.com.cesarsicas.androidmovieflix.presentation.admin.AdminHomeScreen
import br.com.cesarsicas.androidmovieflix.presentation.admin.AdminLoginScreen
import br.com.cesarsicas.androidmovieflix.presentation.admin.AdminWatchPartyScreen
import br.com.cesarsicas.androidmovieflix.presentation.admin.NewTransmissionScreen
import br.com.cesarsicas.androidmovieflix.presentation.admin.UploadMovieScreen

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
        AdminWatchPartyScreen(navController = navController)
    }
    composable(Routes.ADMIN_NEW_TRANSMISSION) {
        NewTransmissionScreen(navController = navController)
    }
    composable(Routes.ADMIN_UPLOAD_MOVIE) {
        UploadMovieScreen(navController = navController)
    }
}
