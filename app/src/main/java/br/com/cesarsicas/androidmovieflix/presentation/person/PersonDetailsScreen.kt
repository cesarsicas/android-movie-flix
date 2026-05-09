package br.com.cesarsicas.androidmovieflix.presentation.person

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.domain.model.PersonModel
import br.com.cesarsicas.androidmovieflix.presentation.common.SectionTitle
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonDetailsScreen(
    personId: Int,
    navController: NavHostController,
    viewModel: PersonDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(personId) { viewModel.load(personId) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    "PERSON",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    letterSpacing = 0.04.sp,
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            ),
            modifier = Modifier.border(1.dp, MaterialTheme.colorScheme.outline),
        )

        when {
            state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
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
    filmography: List<MovieModel>,
    onTitleClick: (Int) -> Unit,
) {
    val initials = person.fullName.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2).joinToString("")

    val professions = listOfNotNull(
        person.mainProfession,
        person.secondaryProfession,
        person.tertiaryProfession,
    ).joinToString(" / ")

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Avatar + header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 24.dp, 24.dp, 14.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(0.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .shadow(elevation = 4.dp, shape = CircleShape)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    if (!person.headshotUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = person.headshotUrl,
                            contentDescription = person.fullName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        Text(
                            initials,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 36.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )
                    }
                }

                Spacer(Modifier.height(14.dp))

                if (professions.isNotEmpty()) {
                    Text(
                        "◉ ${professions.uppercase()}",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        letterSpacing = 0.15.sp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Spacer(Modifier.height(6.dp))
                }

                Text(
                    person.fullName.uppercase(),
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )

                Spacer(Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (person.dateOfBirth != null) {
                        VhsPersonTag("BORN ${person.dateOfBirth}")
                    }
                    val gender = when (person.gender) {
                        "m" -> "MALE"
                        "f" -> "FEMALE"
                        else -> person.gender?.uppercase()
                    }
                    if (gender != null) VhsPersonTag(gender)
                    if (person.placeOfBirth != null) VhsPersonTag(person.placeOfBirth)
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }

        // Biography section
        item {
            SectionTitle(label = "Biography", num = "01")
            Text(
                text = listOfNotNull(
                    person.placeOfBirth?.let { "Born in $it." },
                    person.dateOfBirth?.let { "Date of birth: $it." },
                    person.dateOfDeath?.let { "Died: $it." },
                    if (professions.isNotEmpty()) "Known for: $professions." else null,
                ).joinToString(" ").ifBlank { "No biography available." },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
            )
            Spacer(Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }

        // Filmography section title
        item {
            SectionTitle(label = "Filmography · ${filmography.size}", num = "02")
        }

        // Filmography list
        items(filmography) { movie ->
            FilmographyRow(movie = movie, onClick = { onTitleClick(movie.externalId) })
        }

        item { Spacer(Modifier.height(80.dp)) }
    }
}

@Composable
private fun FilmographyRow(movie: MovieModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(0.dp),
            )
            .padding(10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top,
    ) {
        AsyncImage(
            model = movie.posterUrl.ifBlank { null },
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(48.dp)
                .aspectRatio(2f / 3f)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                movie.title,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                buildString {
                    if (movie.releaseDate.length >= 4) append(movie.releaseDate.take(4))
                    if (movie.type.isNotBlank()) append(" · ${movie.type.uppercase()}")
                },
                fontFamily = FontFamily.Monospace,
                fontSize = 10.sp,
                letterSpacing = 0.1.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun VhsPersonTag(text: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(0.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text,
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            letterSpacing = 0.14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
