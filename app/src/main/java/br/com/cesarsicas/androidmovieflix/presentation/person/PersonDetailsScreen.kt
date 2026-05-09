package br.com.cesarsicas.androidmovieflix.presentation.person

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.domain.model.PersonModel
import br.com.cesarsicas.androidmovieflix.presentation.common.AppTopBar
import br.com.cesarsicas.androidmovieflix.presentation.common.MovieItem
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes
import coil.compose.AsyncImage

@Composable
fun PersonDetailsScreen(
    personId: Int,
    navController: NavHostController,
    viewModel: PersonDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(personId) { viewModel.load(personId) }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            onSearchSubmit = { q -> navController.navigate(Routes.titleSearch(q)) },
            onWatchPartyClick = { navController.navigate(Routes.WATCH_PARTY) },
            onLogoClick = { navController.navigate(Routes.HOME) },
            onAuthClick = { navController.navigate(Routes.PROFILE) },
            onBrowseClick = { navController.navigate(Routes.BROWSE) },
            isLoggedIn = false,
        )

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error!!, modifier = Modifier.padding(16.dp))
            }
            state.person != null -> PersonContent(
                person = state.person!!,
                filmography = state.filmography,
                onTitleClick = { externalId -> navController.navigate(Routes.titleDetails(externalId)) },
            )
        }
    }
}

@Composable
private fun PersonContent(
    person: PersonModel,
    filmography: List<br.com.cesarsicas.androidmovieflix.domain.model.MovieModel>,
    onTitleClick: (Int) -> Unit,
) {
    val professions = listOfNotNull(
        person.mainProfession,
        person.secondaryProfession,
        person.tertiaryProfession,
    ).joinToString(", ")

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = person.headshotUrl,
                contentDescription = person.fullName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color.DarkGray),
            )
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    person.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
                if (professions.isNotEmpty()) {
                    Text(professions, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (person.gender != null) {
                    InfoRow("Gender", if (person.gender == "m") "Male" else if (person.gender == "f") "Female" else person.gender)
                }
                if (person.dateOfBirth != null) InfoRow("Born", person.dateOfBirth)
                if (person.dateOfDeath != null) InfoRow("Died", person.dateOfDeath)
                if (person.placeOfBirth != null) InfoRow("Birthplace", person.placeOfBirth)
            }
        }

        if (filmography.isNotEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Text(
                "Filmography (${filmography.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxWidth().aspectRatio(1f),
                userScrollEnabled = false,
            ) {
                items(filmography) { movie ->
                    MovieItem(
                        movie = movie,
                        onClick = { onTitleClick(movie.externalId) },
                        modifier = Modifier.padding(4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("$label:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
