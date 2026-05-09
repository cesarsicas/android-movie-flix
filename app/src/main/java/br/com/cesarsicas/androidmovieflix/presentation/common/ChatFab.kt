package br.com.cesarsicas.androidmovieflix.presentation.common

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ChatFab(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        text = { Text("AI Chat") },
        icon = {},
    )
}
