package br.com.cesarsicas.androidmovieflix.presentation.watchparty

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import br.com.cesarsicas.androidmovieflix.presentation.common.HlsPlayer
import kotlin.math.abs
import kotlin.math.sin
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchPartyScreen(
    navController: NavHostController,
    viewModel: WatchPartyViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    DisposableEffect(Unit) {
        viewModel.onScreenVisible()
        onDispose { viewModel.onScreenHidden() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    "WATCH PARTY",
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(14.dp),
        ) {
            // Channel strip
            ChannelStrip(isLive = state.transmission != null)

            Spacer(Modifier.height(12.dp))

            // CRT player frame
            CrtPlayerFrame(
                isLive = state.transmission != null,
                isLoading = state.isLoading,
                movieName = state.transmission?.movieName,
                streamUrl = if (state.transmission != null) viewModel.streamUrl else null,
            )

            Spacer(Modifier.height(12.dp))

            when {
                state.isLoading -> {
                    Box(Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text(
                            "LOADING…",
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            letterSpacing = 0.15.sp,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                state.transmission != null -> {
                    TransmissionStatusCard(
                        movieName = state.transmission!!.movieName,
                        elapsed = WatchPartyViewModel.formatElapsed(state.elapsedSeconds),
                    )
                    Spacer(Modifier.height(12.dp))
                    SignalMeter()
                    Spacer(Modifier.height(12.dp))
                    VhsSnackbar(text = "POLLING EVERY 5s · LIVE")
                }

                else -> {
                    Text(
                        "No live stream right now. Check back later!",
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp,
                        letterSpacing = 0.1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                    Spacer(Modifier.height(12.dp))
                    VhsSnackbar(text = "POLLING EVERY 5s · WAITING")
                }
            }

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ChannelStrip(isLive: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            "CH 09",
            fontFamily = FontFamily.Monospace,
            fontSize = 14.sp,
            letterSpacing = 0.1.sp,
            color = MaterialTheme.colorScheme.primary,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (isLive) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.error, RoundedCornerShape(50))
                        .padding(horizontal = 4.dp, vertical = 2.dp),
                ) {
                    Text("●", fontSize = 8.sp, color = Color.White)
                }
            }
            Text(
                if (isLive) "ON AIR" else "STANDBY",
                fontFamily = FontFamily.Monospace,
                fontSize = 13.sp,
                letterSpacing = 0.1.sp,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            "HLS",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            letterSpacing = 0.18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun CrtPlayerFrame(
    isLive: Boolean,
    isLoading: Boolean,
    movieName: String?,
    streamUrl: String?,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color.Black)
            .border(2.dp, Color(0xFF0A0907), RoundedCornerShape(0.dp)),
    ) {
        if (isLive && streamUrl != null) {
            HlsPlayer(
                streamUrl = streamUrl,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            // Static background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1A1A1A)),
            )
        }
        // Scanlines overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = List(30) { i ->
                            if (i % 2 == 0) Color.Black.copy(alpha = 0.18f) else Color.Transparent
                        },
                    ),
                ),
        )
        if (!isLive) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    if (isLoading) "LOADING…" else "NO SIGNAL",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color(0xFFF3EAD2),
                )
            }
        } else if (movieName != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
            ) {
                Text(
                    "▶ LIVE",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    letterSpacing = 0.15.sp,
                    color = Color(0xFFF5C542),
                )
                Text(
                    movieName.uppercase(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    letterSpacing = 0.1.sp,
                    color = Color(0xFFF3EAD2),
                )
            }
        }
    }
}

@Composable
private fun TransmissionStatusCard(movieName: String, elapsed: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.error)
                    .padding(horizontal = 6.dp, vertical = 2.dp),
            ) {
                Text(
                    "● TRANSMISSION",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    letterSpacing = 0.18.sp,
                    color = Color.White,
                )
            }
        }
        StatusRow(label = "STATUS", value = "ON AIR", valueColor = MaterialTheme.colorScheme.error)
        StatusRow(label = "ELAPSED", value = elapsed, valueColor = MaterialTheme.colorScheme.primary)
        StatusRow(label = "MOVIE", value = movieName, valueColor = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun StatusRow(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
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
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
        )
    }
}

@Composable
private fun SignalMeter() {
    val barCount = 24
    val clipStart = 20

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(0.dp))
            .padding(14.dp),
    ) {
        Text(
            "● SIGNAL",
            fontFamily = FontFamily.Monospace,
            fontSize = 11.sp,
            letterSpacing = 0.18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.Bottom,
        ) {
            for (i in 0 until barCount) {
                val barHeight = (8 + abs(sin(i * 0.6)) * 30).dp
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(barHeight)
                        .background(
                            if (i >= clipStart) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.primary,
                        ),
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            listOf("−40", "0dB", "+12").forEach { label ->
                Text(
                    label,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 9.sp,
                    letterSpacing = 0.18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun VhsSnackbar(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.onSurface)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text,
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            letterSpacing = 0.1.sp,
            color = MaterialTheme.colorScheme.surface,
        )
        Text(
            "OK",
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
        )
    }
}
