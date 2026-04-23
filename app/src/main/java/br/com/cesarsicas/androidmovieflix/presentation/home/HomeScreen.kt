package br.com.cesarsicas.androidmovieflix.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.presentation.common.AppTopBar
import br.com.cesarsicas.androidmovieflix.presentation.common.Banner
import br.com.cesarsicas.androidmovieflix.presentation.common.SectionContainer
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes

@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            onSearchSubmit = { query -> navController.navigate(Routes.titleSearch(query)) },
            onWatchPartyClick = { navController.navigate(Routes.WATCH_PARTY) },
            onLogoClick = { navController.navigate(Routes.HOME) },
            onAuthClick = {
                if (isLoggedIn) navController.navigate(Routes.PROFILE)
                else navController.navigate(Routes.auth())
            },
            isLoggedIn = isLoggedIn,
        )
        when (val s = state) {
            is UiState.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator() }

            is UiState.Error -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) { Text(s.message, modifier = Modifier.padding(16.dp)) }

            is UiState.Success -> HomeContent(
                movies = s.data,
                onMovieClick = { movie ->
                    navController.navigate(Routes.titleDetails(movie.externalId))
                },
            )
        }
    }
}

@Composable
private fun HomeContent(
    movies: List<MovieModel>,
    onMovieClick: (MovieModel) -> Unit,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        Banner()
        SectionContainer(
            title = "New Releases",
            movies = movies.slice(0..minOf(9, movies.lastIndex)),
            onMovieClick = onMovieClick,
        )
        SectionContainer(
            title = "Popular Movies",
            movies = movies.slice(
                minOf(11, movies.size)..minOf(19, movies.lastIndex)
            ).takeIf { it.isNotEmpty() } ?: emptyList(),
            onMovieClick = onMovieClick,
        )
        SectionContainer(
            title = "Trending",
            movies = movies.slice(
                minOf(21, movies.size)..minOf(29, movies.lastIndex)
            ).takeIf { it.isNotEmpty() } ?: emptyList(),
            onMovieClick = onMovieClick,
        )
    }
}
