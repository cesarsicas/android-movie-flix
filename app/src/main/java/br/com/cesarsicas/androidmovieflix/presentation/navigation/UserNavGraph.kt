package br.com.cesarsicas.androidmovieflix.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

fun NavGraphBuilder.userGraph(navController: NavHostController) {
    composable(Routes.HOME) {
        PlaceholderScreen(
            navController = navController,
            title = "Home",
        )
    }
    composable(Routes.AUTH) {
        PlaceholderScreen(navController = navController, title = "Auth")
    }
    composable(
        route = Routes.TITLE_DETAILS_TEMPLATE,
        arguments = listOf(navArgument(Routes.TITLE_DETAILS_ARG) { type = NavType.StringType }),
    ) { entry ->
        val externalId = entry.arguments?.getString(Routes.TITLE_DETAILS_ARG).orEmpty()
        PlaceholderScreen(
            navController = navController,
            title = "Title Details (externalId=$externalId)",
        )
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
        PlaceholderScreen(
            navController = navController,
            title = "Search Results (query=$query)",
        )
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
