package br.com.cesarsicas.androidmovieflix.presentation.watchparty

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.presentation.common.HlsPlayer

@Composable
fun WatchPartyScreen(
    navController: NavHostController,
    viewModel: WatchPartyViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Watch Party",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        when {
            state.isLoading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            state.transmission != null -> {
                HlsPlayer(
                    streamUrl = viewModel.streamUrl,
                    modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                )
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        state.transmission!!.movieName,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    Text(
                        "Elapsed: ${WatchPartyViewModel.formatElapsed(state.elapsedSeconds)}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            else -> Text(
                "No live stream right now. Check back later!",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
