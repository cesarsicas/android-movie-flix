package br.com.cesarsicas.androidmovieflix.presentation.browse

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.presentation.common.AppTopBar
import br.com.cesarsicas.androidmovieflix.presentation.common.MovieItem
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes
import androidx.compose.foundation.BorderStroke

private val TITLE_TYPES = listOf(
    null to "All",
    "movie" to "Movie",
    "tv_series" to "TV Series",
    "tv_miniseries" to "Mini Series",
    "tv_special" to "TV Special",
    "short_film" to "Short Film",
)

private val SORT_OPTIONS = listOf(
    null to "Default",
    "popularity_desc" to "Most Popular",
    "popularity_asc" to "Least Popular",
    "release_date_desc" to "Newest",
    "release_date_asc" to "Oldest",
    "title_asc" to "Title A–Z",
    "title_desc" to "Title Z–A",
)

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    navController: NavHostController,
    viewModel: BrowseViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val gridState = rememberLazyGridState()
    var filtersExpanded by remember { mutableStateOf(false) }
    var sortMenuExpanded by remember { mutableStateOf(false) }
    var localType by remember { mutableStateOf<String?>(null) }
    var localGenres by remember { mutableStateOf(emptySet<Int>()) }
    var localSort by remember { mutableStateOf<String?>(null) }
    var localRating by remember { mutableStateOf(0f..10f) }

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisible ->
                if (lastVisible != null && lastVisible >= state.titles.size - 4 && !state.isLoading) {
                    viewModel.loadNextPage()
                }
            }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppTopBar(
            onSearchSubmit = { q -> navController.navigate(Routes.titleSearch(q)) },
            onWatchPartyClick = { navController.navigate(Routes.WATCH_PARTY) },
            onLogoClick = { navController.navigate(Routes.HOME) },
            onAuthClick = { navController.navigate(Routes.PROFILE) },
            onBrowseClick = { navController.navigate(Routes.BROWSE) },
            isLoggedIn = false,
        )

        // Type filter chips
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 8.dp)) {
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                TITLE_TYPES.forEach { (value, label) ->
                    FilterChip(
                        selected = localType == value,
                        onClick = {
                            localType = value
                            viewModel.applyFilters(type = value, genreIds = localGenres, sortBy = localSort)
                        },
                        label = {
                            Text(
                                label.uppercase(),
                                fontFamily = FontFamily.Monospace,
                                fontSize = 11.sp,
                                letterSpacing = 0.12.sp,
                            )
                        },
                        shape = RoundedCornerShape(0.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = localType == value,
                            borderColor = MaterialTheme.colorScheme.outline,
                            selectedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                    )
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 8.dp),
            ) {
                Box {
                    OutlinedButton(
                        onClick = { sortMenuExpanded = true },
                        shape = RoundedCornerShape(0.dp),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    ) {
                        Text(
                            "SORT: ${SORT_OPTIONS.find { it.first == localSort }?.second?.uppercase() ?: "DEFAULT"}",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            letterSpacing = 0.12.sp,
                        )
                    }
                    DropdownMenu(expanded = sortMenuExpanded, onDismissRequest = { sortMenuExpanded = false }) {
                        SORT_OPTIONS.forEach { (value, label) ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        label,
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 12.sp,
                                    )
                                },
                                onClick = {
                                    localSort = value
                                    sortMenuExpanded = false
                                    viewModel.applyFilters(type = localType, genreIds = localGenres, sortBy = value)
                                },
                            )
                        }
                    }
                }
                OutlinedButton(
                    onClick = { filtersExpanded = !filtersExpanded },
                    shape = RoundedCornerShape(0.dp),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                ) {
                    Text(
                        if (filtersExpanded) "HIDE FILTERS" else "MORE FILTERS",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        letterSpacing = 0.12.sp,
                    )
                }
            }

            if (filtersExpanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.outline,
                )

                if (state.genres.isNotEmpty()) {
                    Text(
                        "GENRES",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        letterSpacing = 0.18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp),
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        state.genres.forEach { genre ->
                            FilterChip(
                                selected = genre.id in localGenres,
                                onClick = {
                                    localGenres = if (genre.id in localGenres) localGenres - genre.id
                                    else localGenres + genre.id
                                    viewModel.applyFilters(type = localType, genreIds = localGenres, sortBy = localSort)
                                },
                                label = {
                                    Text(
                                        genre.name.uppercase(),
                                        fontFamily = FontFamily.Monospace,
                                        fontSize = 11.sp,
                                        letterSpacing = 0.12.sp,
                                    )
                                },
                                shape = RoundedCornerShape(0.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = genre.id in localGenres,
                                    borderColor = MaterialTheme.colorScheme.outline,
                                    selectedBorderColor = MaterialTheme.colorScheme.primary,
                                ),
                            )
                        }
                    }
                }

                Text(
                    "RATING ★ ${localRating.start.toInt()}–${localRating.endInclusive.toInt()}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    letterSpacing = 0.18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                )
                RangeSlider(
                    value = localRating,
                    onValueChange = { localRating = it },
                    onValueChangeFinished = {
                        viewModel.applyFilters(
                            type = localType,
                            genreIds = localGenres,
                            sortBy = localSort,
                            ratingLow = localRating.start,
                            ratingHigh = localRating.endInclusive,
                        )
                    },
                    valueRange = 0f..10f,
                    steps = 9,
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outline)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                "${state.totalResults} TITLES",
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                letterSpacing = 0.18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        if (state.error != null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(state.error!!, modifier = Modifier.padding(16.dp))
            }
        } else {
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize(),
            ) {
                items(state.titles) { movie ->
                    MovieItem(
                        movie = movie,
                        onClick = { navController.navigate(Routes.titleDetails(movie.externalId)) },
                    )
                }
                if (state.isLoading) {
                    item {
                        Box(
                            modifier = Modifier.padding(16.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp,
                            )
                        }
                    }
                }
            }
        }
    }
}
