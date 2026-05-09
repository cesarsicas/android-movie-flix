package br.com.cesarsicas.androidmovieflix.presentation.common

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ChatFab(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        shape = RoundedCornerShape(0.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
        ),
        modifier = Modifier
            .size(56.dp)
            .shadow(elevation = 4.dp, spotColor = MaterialTheme.colorScheme.onSurface)
            .border(2.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(0.dp)),
    ) {
        Text("✦", fontSize = 22.sp, color = MaterialTheme.colorScheme.onPrimary)
    }
}
