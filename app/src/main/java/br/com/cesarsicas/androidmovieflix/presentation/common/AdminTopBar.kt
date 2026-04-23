package br.com.cesarsicas.androidmovieflix.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminTopBar(
    onLogoClick: () -> Unit,
    onWatchPartyClick: () -> Unit,
    onLogout: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                "MovieFlix Admin",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.clickable { onLogoClick() },
            )
        },
        actions = {
            TextButton(onClick = onWatchPartyClick) { Text("Watch Party") }
            TextButton(onClick = onLogout) { Text("Logout") }
        },
    )
}
