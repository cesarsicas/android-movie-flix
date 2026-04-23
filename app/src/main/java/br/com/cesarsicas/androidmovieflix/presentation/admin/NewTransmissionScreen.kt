package br.com.cesarsicas.androidmovieflix.presentation.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import br.com.cesarsicas.androidmovieflix.domain.model.WatchPartyMovieModel
import br.com.cesarsicas.androidmovieflix.presentation.common.RequireAdminAuth
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes

@Composable
fun NewTransmissionScreen(
    navController: NavHostController,
    viewModel: NewTransmissionViewModel = hiltViewModel(),
) {
    RequireAdminAuth(navController) {
        val state by viewModel.state.collectAsState()

        LaunchedEffect(Unit) {
            viewModel.startSuccess.collect {
                navController.navigate(Routes.ADMIN_WATCH_PARTY) {
                    popUpTo(Routes.ADMIN_WATCH_PARTY) { inclusive = true }
                }
            }
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                "New Transmission",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp),
            )

            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                label = { Text("Search movies") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
            )

            when {
                state.isLoading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                state.error != null -> Text(
                    state.error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )

                state.filteredMovies.isEmpty() -> Text(
                    "No movies found.",
                    style = MaterialTheme.typography.bodyMedium,
                )

                else -> {
                    MovieTableHeader()
                    HorizontalDivider()
                    LazyColumn {
                        itemsIndexed(state.filteredMovies) { index, movie ->
                            MovieRow(
                                movie = movie,
                                isEven = index % 2 == 0,
                                isStarting = state.isStarting,
                                onSelect = { viewModel.selectMovie(movie.id) },
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieTableHeader() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text("Title", style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(2f))
        Text("Duration", style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1f))
        Text("File", style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1f))
        Text("", modifier = Modifier.weight(1f))
    }
}

@Composable
private fun MovieRow(
    movie: WatchPartyMovieModel,
    isEven: Boolean,
    isStarting: Boolean,
    onSelect: () -> Unit,
) {
    val bgColor = if (isEven) MaterialTheme.colorScheme.surface
    else MaterialTheme.colorScheme.surfaceVariant

    Row(
        modifier = Modifier.fillMaxWidth().background(bgColor).padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(movie.title, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(2f))
        Text(formatDuration(movie.duration), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(movie.filename, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
        Button(
            onClick = onSelect,
            enabled = !isStarting,
            modifier = Modifier.weight(1f),
        ) {
            Text("Select")
        }
    }
}

private fun formatDuration(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}
