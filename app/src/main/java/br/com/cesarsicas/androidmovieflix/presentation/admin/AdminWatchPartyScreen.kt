package br.com.cesarsicas.androidmovieflix.presentation.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes

@Composable
fun AdminWatchPartyScreen(
    navController: NavHostController,
    viewModel: AdminWatchPartyViewModel = hiltViewModel(),
) {
    AdminScaffold(navController) {
        val state by viewModel.state.collectAsState()

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Watch Party", style = MaterialTheme.typography.headlineMedium)
                OutlinedButton(onClick = { navController.navigate(Routes.ADMIN_UPLOAD_MOVIE) }) {
                    Text("Upload New Movie")
                }
            }

            when {
                state.isLoading -> Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }

                state.transmission != null -> {
                    val tx = state.transmission!!
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                "LIVE",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red,
                            )
                            Text(tx.movieName, style = MaterialTheme.typography.titleLarge)
                            Text(
                                "Started: ${AdminWatchPartyViewModel.formatStartTime(tx.startTime)}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                            Text(
                                "Duration: ${AdminWatchPartyViewModel.formatDuration(tx.duration)}",
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                    Button(
                        onClick = { viewModel.stopTransmission() },
                        enabled = !state.isStopping,
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    ) {
                        Text(if (state.isStopping) "Stopping..." else "Stop Transmission")
                    }
                }

                else -> {
                    Text(
                        "No active transmission.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(bottom = 16.dp),
                    )
                    Button(
                        onClick = { navController.navigate(Routes.ADMIN_NEW_TRANSMISSION) },
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Create New Transmission")
                    }
                }
            }

            state.error?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}
