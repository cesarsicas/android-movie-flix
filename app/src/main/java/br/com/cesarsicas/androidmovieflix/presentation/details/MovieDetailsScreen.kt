package br.com.cesarsicas.androidmovieflix.presentation.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.domain.model.TitleDetailsModel
import br.com.cesarsicas.androidmovieflix.presentation.common.Banner
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import coil.compose.AsyncImage

@Composable
fun MovieDetailsScreen(
    externalId: Int,
    navController: NavHostController,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(externalId) { viewModel.loadDetails(externalId) }

    when (val s = state) {
        is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(s.message, modifier = Modifier.padding(16.dp))
        }
        is UiState.Success -> MovieDetailsContent(details = s.data)
    }
}

@Composable
private fun MovieDetailsContent(details: TitleDetailsModel) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Banner(backdropUrl = details.poster)

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AsyncImage(
                model = details.poster,
                contentDescription = details.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(100.dp).aspectRatio(2f / 3f),
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(details.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                if (details.originalTitle != details.title) {
                    Text(details.originalTitle, style = MaterialTheme.typography.bodyMedium)
                }
                details.year?.let { Text("$it", style = MaterialTheme.typography.bodySmall) }
                Text(
                    details.type.capitalize(Locale.current),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Synopsis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(details.plotOverview, style = MaterialTheme.typography.bodyMedium)
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Cast", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Cast information not available.", style = MaterialTheme.typography.bodyMedium)
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text("Watch", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Video player coming in Phase 4.", style = MaterialTheme.typography.bodySmall)
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text("Movie Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                DetailRow("Genre", details.genreNames?.joinToString(", ") ?: "N/A")
                DetailRow("Year", details.year?.toString() ?: "N/A")
                DetailRow("Duration", details.runtimeMinutes?.let { formatRuntime(it) } ?: "N/A")
                DetailRow("Rating", details.userRating?.toString() ?: "N/A")
            }
        }

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Reviews", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("Reviews coming in Phase 4.", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "$label:",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

private fun formatRuntime(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}
