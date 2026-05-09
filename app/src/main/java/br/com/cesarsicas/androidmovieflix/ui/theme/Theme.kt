package br.com.cesarsicas.androidmovieflix.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = VhsPrimaryDark,
    onPrimary = VhsOnPrimaryDark,
    primaryContainer = VhsPrimaryContainerDark,
    onPrimaryContainer = VhsOnSurface,
    secondary = VhsSecondary,
    onSecondary = VhsOnPrimaryDark,
    tertiary = VhsTertiary,
    onTertiary = VhsOnSurface,
    error = VhsError,
    onError = VhsOnSurface,
    background = VhsSurface,
    onBackground = VhsOnSurface,
    surface = VhsSurface,
    onSurface = VhsOnSurface,
    surfaceVariant = VhsSurface2,
    onSurfaceVariant = VhsOnSurfaceVariant,
    surfaceContainer = VhsSurfaceContainer,
    surfaceContainerHigh = VhsSurface1,
    surfaceContainerHighest = VhsSurface2,
    outline = VhsOutline,
    outlineVariant = VhsOutlineVariant,
    inverseSurface = VhsOnSurface,
    inverseOnSurface = VhsSurface,
)

private val LightColorScheme = lightColorScheme(
    primary = VhsPrimaryLight,
    onPrimary = VhsOnPrimaryLight,
    primaryContainer = VhsPrimaryContainerLight,
    onPrimaryContainer = VhsOnSurfaceLight,
    secondary = VhsSecondary,
    onSecondary = VhsOnSurfaceLight,
    tertiary = VhsTertiary,
    onTertiary = VhsOnSurfaceLight,
    error = VhsError,
    onError = VhsOnSurfaceLight,
    background = VhsSurfaceLight,
    onBackground = VhsOnSurfaceLight,
    surface = VhsSurfaceLight,
    onSurface = VhsOnSurfaceLight,
    surfaceVariant = VhsSurface2Light,
    onSurfaceVariant = VhsOnSurfaceVariantLight,
    surfaceContainer = VhsSurfaceContainerLight,
    surfaceContainerHigh = VhsSurface1Light,
    surfaceContainerHighest = VhsSurface2Light,
    outline = VhsOutlineLight,
    outlineVariant = VhsOutlineVariantLight,
    inverseSurface = VhsOnSurfaceLight,
    inverseOnSurface = VhsSurfaceLight,
)

@Composable
fun AndroidMovieFlixTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
