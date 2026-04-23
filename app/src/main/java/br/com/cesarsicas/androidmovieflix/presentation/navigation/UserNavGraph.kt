package br.com.cesarsicas.androidmovieflix.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.cesarsicas.androidmovieflix.presentation.details.MovieDetailsScreen
import br.com.cesarsicas.androidmovieflix.presentation.home.HomeScreen
import br.com.cesarsicas.androidmovieflix.presentation.search.SearchResultScreen

fun NavGraphBuilder.userGraph(navController: NavHostController) {
    composable(Routes.HOME) {
        HomeScreen(navController = navController)
    }
    composable(
        route = Routes.AUTH_TEMPLATE,
        arguments = listOf(
            navArgument(Routes.AUTH_MODE_ARG) {
                type = NavType.StringType
                defaultValue = "login"
            },
        ),
    ) {
        PlaceholderScreen(navController = navController, title = "Auth")
    }
    composable(
        route = Routes.TITLE_DETAILS_TEMPLATE,
        arguments = listOf(navArgument(Routes.TITLE_DETAILS_ARG) { type = NavType.IntType }),
    ) { entry ->
        val externalId = entry.arguments?.getInt(Routes.TITLE_DETAILS_ARG) ?: 0
        MovieDetailsScreen(externalId = externalId, navController = navController)
    }
    composable(
        route = Routes.TITLE_SEARCH_TEMPLATE,
        arguments = listOf(
            navArgument(Routes.TITLE_SEARCH_ARG) {
                type = NavType.StringType
                defaultValue = ""
            },
        ),
    ) { entry ->
        val query = entry.arguments?.getString(Routes.TITLE_SEARCH_ARG).orEmpty()
        SearchResultScreen(query = query, navController = navController)
    }
    composable(Routes.PROFILE) {
        PlaceholderScreen(navController = navController, title = "Profile")
    }
    composable(Routes.PROFILE_EDIT) {
        PlaceholderScreen(navController = navController, title = "Profile Edit")
    }
    composable(Routes.WATCH_PARTY) {
        PlaceholderScreen(navController = navController, title = "Watch Party")
    }
}
