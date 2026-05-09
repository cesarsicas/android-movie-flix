package br.com.cesarsicas.androidmovieflix.presentation.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.domain.model.CastMemberModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleDetailsModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleReviewModel
import br.com.cesarsicas.androidmovieflix.presentation.common.Banner
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes
import coil.compose.AsyncImage

@Composable
fun MovieDetailsScreen(
    externalId: Int,
    navController: NavHostController,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    LaunchedEffect(externalId) { viewModel.loadDetails(externalId) }

    if (state.showReviewDialog) {
        ReviewDialog(
            isSaving = state.isSavingReview,
            error = state.reviewError,
            onSubmit = { rating, review -> viewModel.submitReview(rating, review) },
            onDismiss = { viewModel.closeReviewDialog() },
        )
    }

    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(state.error!!, modifier = Modifier.padding(16.dp))
        }
        state.details != null -> MovieDetailsContent(
            details = state.details!!,
            reviews = state.reviews,
            streamUrl = state.streamUrl,
            isLoggedIn = isLoggedIn,
            onWriteReview = { viewModel.openReviewDialog() },
            onPersonClick = { personId -> navController.navigate(Routes.personDetails(personId)) },
        )
    }
}

@Composable
private fun MovieDetailsContent(
    details: TitleDetailsModel,
    reviews: List<TitleReviewModel>,
    streamUrl: String?,
    isLoggedIn: Boolean,
    onWriteReview: () -> Unit,
    onPersonClick: (Int) -> Unit,
) {
    val context = LocalContext.current

    val actors = details.cast?.filter { it.order == 1 && it.role?.contains("Director") != true && it.role?.contains("Writer") != true }
    val crew = details.cast?.filter {
        it.order == 1 && (it.role?.contains("Director") == true || it.role?.contains("Writer") == true)
    }?.distinctBy { it.personId }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Banner(backdropUrl = details.backdrop ?: details.poster)

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
                Text(
                    details.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                )
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

        if (!details.trailerThumbnail.isNullOrBlank()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Trailer", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clickable(enabled = !details.trailer.isNullOrBlank()) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(details.trailer))
                            context.startActivity(intent)
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    AsyncImage(
                        model = details.trailerThumbnail,
                        contentDescription = "Trailer thumbnail",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Play trailer",
                        modifier = Modifier.size(64.dp),
                        tint = Color.White.copy(alpha = 0.9f),
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }

        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Synopsis", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(details.plotOverview, style = MaterialTheme.typography.bodyMedium)
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        if (!actors.isNullOrEmpty()) {
            Column(modifier = Modifier.padding(vertical = 8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Cast",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(actors) { member ->
                        CastCard(member = member, onClick = { onPersonClick(member.personId) })
                    }
                }
            }
        } else {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Cast", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Cast information not available.", style = MaterialTheme.typography.bodyMedium)
            }
        }

        if (!crew.isNullOrEmpty()) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Crew", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                crew.forEach { member ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            "${member.role}:",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            member.name,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.clickable { onPersonClick(member.personId) },
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    "Movie Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                DetailRow("Genre", details.genreNames?.joinToString(", ") ?: "N/A")
                DetailRow("Year", details.year?.toString() ?: "N/A")
                DetailRow("Duration", details.runtimeMinutes?.let { formatRuntime(it) } ?: "N/A")
                DetailRow("Rating", details.userRating?.toString() ?: "N/A")
            }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "Reviews",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                if (isLoggedIn) {
                    Button(onClick = onWriteReview) { Text("Write a Review") }
                }
            }

            if (reviews.isEmpty()) {
                Text("No reviews yet. Be the first!", style = MaterialTheme.typography.bodyMedium)
            } else {
                reviews.forEach { ReviewItem(it) }
            }
        }
    }
}

@Composable
private fun CastCard(member: CastMemberModel, onClick: () -> Unit) {
    Column(
        modifier = Modifier.width(80.dp).clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        AsyncImage(
            model = member.imageUrl,
            contentDescription = member.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2f / 3f)
                .background(Color.DarkGray),
        )
        Text(
            member.name,
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        member.role?.let {
            Text(
                it,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun ReviewItem(review: TitleReviewModel) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    review.userName ?: "Anonymous",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                review.rating?.let { Text("$it/10", style = MaterialTheme.typography.bodySmall) }
            }
            review.review?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
            review.createdAt?.let { Text(it, style = MaterialTheme.typography.labelSmall) }
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
