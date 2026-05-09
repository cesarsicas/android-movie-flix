package br.com.cesarsicas.androidmovieflix.presentation.home

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel
import br.com.cesarsicas.androidmovieflix.presentation.common.AppTopBar
import br.com.cesarsicas.androidmovieflix.presentation.common.SectionContainer
import br.com.cesarsicas.androidmovieflix.presentation.common.SectionTitle
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes
import coil.compose.AsyncImage

private val MARQUEE_TEXT = listOf(
    "★ TONIGHT 9PM · NEW RELEASES",
    "•",
    "NEW THIS WEEK · BROWSE ALL",
    "•",
    "BE KIND, REWIND",
    "•",
    "★ WATCH PARTY LIVE NOW",
    "•",
)

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
            onBrowseClick = { navController.navigate(Routes.BROWSE) },
        )

        when (val s = state) {
            is UiState.Loading -> Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.primary) }

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
private fun MarqueeStrip() {
    val infiniteTransition = rememberInfiniteTransition(label = "marquee")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 18000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "marquee_offset",
    )
    val text = MARQUEE_TEXT.joinToString("   ")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp)
            .background(MaterialTheme.colorScheme.primary)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(0.dp),
            ),
        contentAlignment = Alignment.CenterStart,
    ) {
        Row(
            modifier = Modifier.graphicsLayer {
                translationX = offset * 2000f
            },
        ) {
            repeat(3) {
                Text(
                    text = text,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    letterSpacing = 0.1.sp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun FeaturedCard(movie: MovieModel, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp)
            .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp))
            .clickable(onClick = onClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 10f),
        ) {
            AsyncImage(
                model = movie.posterUrl.ifBlank { null },
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, MaterialTheme.colorScheme.surface),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY,
                        ),
                    ),
            )
        }
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 14.dp, vertical = 14.dp),
        ) {
            Text(
                text = "◉ TONIGHT'S PICK · CH 09",
                fontFamily = FontFamily.Monospace,
                fontSize = 12.sp,
                letterSpacing = 0.15.sp,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = movie.title.uppercase(),
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                lineHeight = 28.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                VhsSticker(text = movie.type.uppercase())
                if (movie.releaseDate.length >= 4) {
                    VhsPillTag(text = movie.releaseDate.take(4))
                }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        "▶ PLAY",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        letterSpacing = 0.15.sp,
                    )
                }
                OutlinedButton(
                    onClick = onClick,
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.5.dp,
                        MaterialTheme.colorScheme.outline,
                    ),
                ) {
                    Text(
                        "+ QUEUE",
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp,
                        letterSpacing = 0.15.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun VhsSticker(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.primary)
            .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(0.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp)
            .graphicsLayer { rotationZ = -2f },
    ) {
        Text(
            text = text,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.05.sp,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
fun VhsPillTag(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(0.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp),
    ) {
        Text(
            text = text.uppercase(),
            fontFamily = FontFamily.Monospace,
            fontSize = 10.sp,
            letterSpacing = 0.14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun HomeContent(
    movies: List<MovieModel>,
    onMovieClick: (MovieModel) -> Unit,
) {
    val featured = movies.getOrNull(12) ?: movies.firstOrNull()

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        MarqueeStrip()

        if (featured != null) {
            FeaturedCard(movie = featured, onClick = { onMovieClick(featured) })
        }

        SectionContainer(
            title = "New Releases",
            num = "01",
            movies = movies.slice(0..minOf(9, movies.lastIndex)),
            onMovieClick = onMovieClick,
        )

        Spacer(Modifier.height(20.dp))

        SectionContainer(
            title = "Popular",
            num = "02",
            movies = movies.slice(
                minOf(11, movies.size)..minOf(19, movies.lastIndex),
            ).takeIf { it.isNotEmpty() } ?: emptyList(),
            onMovieClick = onMovieClick,
        )

        Spacer(Modifier.height(80.dp))
    }
}
