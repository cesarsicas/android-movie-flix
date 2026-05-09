package br.com.cesarsicas.androidmovieflix.presentation.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.domain.model.CastMemberModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleDetailsModel
import br.com.cesarsicas.androidmovieflix.domain.model.TitleReviewModel
import br.com.cesarsicas.androidmovieflix.presentation.common.SectionTitle
import br.com.cesarsicas.androidmovieflix.presentation.navigation.Routes
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
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
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(state.error!!, modifier = Modifier.padding(16.dp))
        }
        state.details != null -> MovieDetailsContent(
            details = state.details!!,
            reviews = state.reviews,
            streamUrl = state.streamUrl,
            isLoggedIn = isLoggedIn,
            onBack = { navController.popBackStack() },
            onWriteReview = { viewModel.openReviewDialog() },
            onPersonClick = { personId -> navController.navigate(Routes.personDetails(personId)) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieDetailsContent(
    details: TitleDetailsModel,
    reviews: List<TitleReviewModel>,
    streamUrl: String?,
    isLoggedIn: Boolean,
    onBack: () -> Unit,
    onWriteReview: () -> Unit,
    onPersonClick: (Int) -> Unit,
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("OVERVIEW", "TRAILER", "REVIEWS")

    val actors = details.cast?.filter {
        it.role?.contains("Director") != true && it.role?.contains("Writer") != true
    }?.take(10)
    val crew = details.cast?.filter {
        it.role?.contains("Director") == true || it.role?.contains("Writer") == true
    }?.distinctBy { it.personId }?.take(5)

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {

        // Backdrop hero with transparent TopAppBar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
        ) {
            AsyncImage(
                model = details.backdrop ?: details.poster,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, MaterialTheme.colorScheme.surface),
                            startY = 100f,
                        ),
                    ),
            )
            // Transparent top bar overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {}) {
                    Text("↗", fontSize = 18.sp, color = Color.White)
                }
                IconButton(onClick = {}) {
                    Text("⋮", fontSize = 18.sp, color = Color.White)
                }
            }
        }

        // Title block
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-90).dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            Box(
                modifier = Modifier
                    .width(96.dp)
                    .border(1.5.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp)),
            ) {
                AsyncImage(
                    model = details.poster,
                    contentDescription = details.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 3f)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                )
            }
            Column(
                modifier = Modifier.padding(bottom = 6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    "◉ ${details.type.uppercase()} · ${details.year ?: ""}",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    letterSpacing = 0.14.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    details.title.uppercase(),
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 2.dp),
                ) {
                    details.userRating?.let { rating ->
                        VhsDetailSticker("★ ${"%.1f".format(rating)}")
                    }
                    details.runtimeMinutes?.let { rt ->
                        VhsDetailTag("${rt} MIN")
                    }
                }
            }
        }

        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .offset(y = (-80).dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Button(
                onClick = {},
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
                onClick = {},
                shape = RoundedCornerShape(0.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            ) {
                Text("+", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
            }
            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(0.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            ) {
                Text("↗", fontFamily = FontFamily.Monospace, fontSize = 16.sp)
            }
        }

        // Tab row
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = {
                Box(
                    modifier = Modifier
                        .tabIndicatorOffset(selectedTab)
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.primary),
                )
            },
        ) {
            tabs.forEachIndexed { i, title ->
                Tab(
                    selected = selectedTab == i,
                    onClick = { selectedTab = i },
                    text = {
                        Text(
                            title,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            letterSpacing = 0.16.sp,
                            color = if (selectedTab == i) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = if (selectedTab == i) FontWeight.SemiBold else FontWeight.Normal,
                        )
                    },
                )
            }
        }

        // Tab content
        when (selectedTab) {
            0 -> OverviewTab(
                details = details,
                actors = actors,
                crew = crew,
                onPersonClick = onPersonClick,
                context = context,
            )
            1 -> TrailerTab(details = details, context = context)
            2 -> ReviewsTab(reviews = reviews, isLoggedIn = isLoggedIn, onWriteReview = onWriteReview)
        }

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
private fun OverviewTab(
    details: TitleDetailsModel,
    actors: List<CastMemberModel>?,
    crew: List<CastMemberModel>?,
    onPersonClick: (Int) -> Unit,
    context: android.content.Context,
) {
    Column(modifier = Modifier.padding(top = 4.dp)) {
        // Synopsis
        SectionTitle(label = "Synopsis", num = "01")
        Text(
            details.plotOverview,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
        )

        // Details card
        Spacer(Modifier.height(8.dp))
        SectionTitle(label = "Details", num = "02")
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            DetailRow("GENRE", details.genreNames?.joinToString(", ") ?: "N/A")
            DetailRow("YEAR", details.year?.toString() ?: "N/A")
            DetailRow("DURATION", details.runtimeMinutes?.let { formatRuntime(it) } ?: "N/A")
            DetailRow("RATING", details.userRating?.toString() ?: "N/A")
            DetailRow("LANGUAGE", details.originalLanguage?.uppercase() ?: "N/A")
        }

        // Cast
        if (!actors.isNullOrEmpty()) {
            Spacer(Modifier.height(8.dp))
            SectionTitle(label = "Cast", num = "03")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(actors) { member ->
                    CastCard(member = member, onClick = { onPersonClick(member.personId) })
                }
            }
        }

        // Crew
        if (!crew.isNullOrEmpty()) {
            Spacer(Modifier.height(8.dp))
            SectionTitle(label = "Crew", num = "04")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                crew.forEach { member ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPersonClick(member.personId) }
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            (member.role ?: "CREW").uppercase(),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            letterSpacing = 0.18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            member.name,
                            fontFamily = FontFamily.Default,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

@Composable
private fun TrailerTab(details: TitleDetailsModel, context: android.content.Context) {
    Column(modifier = Modifier.padding(top = 4.dp)) {
        SectionTitle(label = "Trailer", num = "01")
        if (!details.trailerThumbnail.isNullOrBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
                    .border(2.dp, Color(0xFF0A0907), RoundedCornerShape(0.dp))
                    .clickable(enabled = !details.trailer.isNullOrBlank()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(details.trailer))
                        context.startActivity(intent)
                    },
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = details.trailerThumbnail,
                    contentDescription = "Trailer",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
                // Scanlines
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = List(30) { i ->
                                    if (i % 2 == 0) Color.Black.copy(alpha = 0.15f) else Color.Transparent
                                },
                            ),
                        ),
                )
                // Play button overlay
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.4f)).fillMaxSize(),
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("▶", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "YOUTUBE",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        letterSpacing = 0.15.sp,
                        color = Color(0xFFF5C542),
                    )
                }
            }
        } else {
            Text(
                "No trailer available.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun ReviewsTab(
    reviews: List<TitleReviewModel>,
    isLoggedIn: Boolean,
    onWriteReview: () -> Unit,
) {
    Column(modifier = Modifier.padding(top = 4.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "REVIEWS",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp,
                letterSpacing = 0.18.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (isLoggedIn) {
                Button(
                    onClick = onWriteReview,
                    shape = RoundedCornerShape(0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                ) {
                    Text(
                        "WRITE REVIEW",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 11.sp,
                        letterSpacing = 0.15.sp,
                    )
                }
            }
        }

        if (reviews.isEmpty()) {
            Text(
                "No reviews yet :(",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        } else {
            reviews.forEach { review ->
                VhsReviewItem(review = review)
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                )
            }
        }
    }
}

@Composable
private fun CastCard(member: CastMemberModel, onClick: () -> Unit) {
    val initials = member.name.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .take(2).joinToString("")

    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp)),
            contentAlignment = Alignment.Center,
        ) {
            if (!member.imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = member.imageUrl,
                    contentDescription = member.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                Text(
                    initials,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Text(
            member.name,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurface,
        )
        member.role?.let {
            Text(
                it,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun VhsReviewItem(review: TitleReviewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(35.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                (review.userName?.firstOrNull()?.uppercase() ?: "?"),
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                review.userName ?: "Anonymous",
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
            review.review?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            label,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            letterSpacing = 0.18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            value,
            fontFamily = FontFamily.Default,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.End,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}

@Composable
private fun VhsDetailSticker(text: String) {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .border(1.dp, MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(0.dp))
            .padding(horizontal = 7.dp, vertical = 2.dp),
    ) {
        Text(
            text,
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}

@Composable
private fun VhsDetailTag(text: String) {
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
        )
    }
}

private fun formatRuntime(minutes: Int): String {
    val h = minutes / 60
    val m = minutes % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}
