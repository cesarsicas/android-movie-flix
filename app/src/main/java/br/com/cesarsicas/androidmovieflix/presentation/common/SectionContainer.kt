package br.com.cesarsicas.androidmovieflix.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.cesarsicas.androidmovieflix.domain.model.MovieModel

@Composable
fun SectionTitle(
    label: String,
    num: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = num,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
        )
        Text(
            text = label.uppercase(),
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            letterSpacing = 0.18.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = MaterialTheme.colorScheme.outlineVariant,
        )
    }
}

@Composable
fun SectionContainer(
    title: String,
    movies: List<MovieModel>,
    onMovieClick: (MovieModel) -> Unit,
    num: String = "··",
    modifier: Modifier = Modifier,
) {
    SectionTitle(label = title, num = num, modifier = modifier)
    LazyRow(
        contentPadding = PaddingValues(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        items(movies) { movie ->
            MovieItem(movie = movie, onClick = { onMovieClick(movie) })
        }
    }
}
