package br.com.cesarsicas.androidmovieflix.presentation.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.domain.usecase.SearchResultItem
import br.com.cesarsicas.androidmovieflix.presentation.common.AppTopBar
import br.com.cesarsicas.androidmovieflix.presentation.common.Banner
import br.com.cesarsicas.androidmovieflix.presentation.common.UiState
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes
import coil.compose.AsyncImage

@Composable
fun SearchResultScreen(
    query: String,
    navController: NavHostController,
    viewModel: SearchResultViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val filter by viewModel.filter.collectAsState()

    LaunchedEffect(query) {
        if (query.isNotBlank()) viewModel.search(query)
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
        Banner()

        PrimaryScrollableTabRow(
            selectedTabIndex = filter.ordinal,
            modifier = Modifier.fillMaxWidth(),
        ) {
            SearchFilter.entries.forEach { f ->
                Tab(
                    selected = filter == f,
                    onClick = { viewModel.setFilter(f) },
                    text = {
                        Text(
                            when (f) {
                                SearchFilter.ALL -> "All"
                                SearchFilter.MOVIES -> "Movies"
                                SearchFilter.TV -> "TV"
                                SearchFilter.PEOPLE -> "People"
                            }
                        )
                    },
                )
            }
        }

        Text(
            text = "Search Results",
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )

        when (val s = state) {
            is UiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is UiState.Error -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(s.message, modifier = Modifier.padding(16.dp))
            }
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
                        items(s.data) { item ->
                            SearchResultCard(
                                item = item,
                                onClick = {
                                    if (item.resultType == "person") {
                                        navController.navigate(Routes.personDetails(item.id))
                                    } else {
                                        navController.navigate(Routes.titleDetails(item.id))
                                    }
                                },
                                modifier = Modifier.padding(4.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultCard(
    item: SearchResultItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(120.dp)
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = item.imageUrl?.ifBlank { null },
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .background(Color.DarkGray),
        )
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 4.dp),
        )
        item.year?.let {
            Text(
                text = "$it",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        }
    }
}
