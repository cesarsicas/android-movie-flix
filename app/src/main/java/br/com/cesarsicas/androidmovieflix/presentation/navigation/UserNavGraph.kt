package br.com.cesarsicas.androidmovieflix.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import br.com.cesarsicas.androidmovieflix.presentation.auth.AuthScreen
import br.com.cesarsicas.androidmovieflix.presentation.browse.BrowseScreen
import br.com.cesarsicas.androidmovieflix.presentation.chat.ChatScreen
import br.com.cesarsicas.androidmovieflix.presentation.details.MovieDetailsScreen
import br.com.cesarsicas.androidmovieflix.presentation.home.HomeScreen
import br.com.cesarsicas.androidmovieflix.presentation.person.PersonDetailsScreen
import br.com.cesarsicas.androidmovieflix.presentation.profile.ProfileEditScreen
import br.com.cesarsicas.androidmovieflix.presentation.profile.ProfileScreen
import br.com.cesarsicas.androidmovieflix.presentation.search.SearchResultScreen
import br.com.cesarsicas.androidmovieflix.presentation.watchparty.WatchPartyScreen

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
    ) { entry ->
        val mode = entry.arguments?.getString(Routes.AUTH_MODE_ARG) ?: "login"
        AuthScreen(mode = mode, navController = navController)
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
    composable(
        route = Routes.PERSON_DETAILS_TEMPLATE,
        arguments = listOf(navArgument(Routes.PERSON_DETAILS_ARG) { type = NavType.IntType }),
    ) { entry ->
        val personId = entry.arguments?.getInt(Routes.PERSON_DETAILS_ARG) ?: 0
        PersonDetailsScreen(personId = personId, navController = navController)
    }
    composable(Routes.BROWSE) {
        BrowseScreen(navController = navController)
    }
    composable(Routes.CHAT) {
        ChatScreen()
    }
    composable(Routes.PROFILE) {
        ProfileScreen(navController = navController)
    }
    composable(Routes.PROFILE_EDIT) {
        ProfileEditScreen(navController = navController)
    }
    composable(Routes.WATCH_PARTY) {
        WatchPartyScreen(navController = navController)
    }
}
