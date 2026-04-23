package br.com.cesarsicas.androidmovieflix.presentation.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.presentation.common.AppTopBar
import br.com.cesarsicas.androidmovieflix.presentation.common.Banner
import br.com.cesarsicas.androidmovieflix.presentation.common.MovieItem
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes

@Composable
fun SearchResultScreen(
    query: String,
    navController: NavHostController,
    viewModel: SearchResultViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(query) {
        if (query.isNotBlank()) viewModel.search(query)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            onSearchSubmit = { q -> navController.navigate(Routes.titleSearch(q)) },
            onWatchPartyClick = { navController.navigate(Routes.WATCH_PARTY) },
            onLogoClick = { navController.navigate(Routes.HOME) },
            onAuthClick = { navController.navigate(Routes.PROFILE) },
            isLoggedIn = false,
        )
        Banner()
        Text(
            text = "Search Results",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
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

            is UiState.Success -> {
                if (s.data.isEmpty()) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No results found.")
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 120.dp),
                        contentPadding = PaddingValues(16.dp),
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        items(s.data) { movie ->
                            MovieItem(
                                movie = movie,
                                onClick = { navController.navigate(Routes.titleDetails(movie.externalId)) },
                                modifier = Modifier.padding(4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
