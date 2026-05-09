package br.com.cesarsicas.androidmovieflix.presentation.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onSearchSubmit: (String) -> Unit,
    onWatchPartyClick: () -> Unit,
    onLogoClick: () -> Unit,
    onAuthClick: () -> Unit,
    isLoggedIn: Boolean,
    onBrowseClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    var searchOpen by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
        ),
        modifier = modifier.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline,
        ),
        title = {
            AnimatedVisibility(visible = searchOpen) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    TextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = {
                            Text(
                                "SEARCH…",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = {
                            if (query.isNotBlank()) {
                                onSearchSubmit(query.trim())
                                searchOpen = false
                                query = ""
                            }
                        }),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.primary,
                        ),
                        textStyle = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                    )
                }
            }
            AnimatedVisibility(visible = !searchOpen) {
                Text(
                    text = "REACTFLIX",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.04.sp,
                        fontSize = 18.sp,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        },
        actions = {
            if (searchOpen) {
                IconButton(onClick = { searchOpen = false; query = "" }) {
                    Icon(Icons.Default.Close, contentDescription = "Close search")
                }
            } else {
                IconButton(onClick = { searchOpen = true }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = onAuthClick) {
                    Text(
                        if (isLoggedIn) "◉" else "→",
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
    )
}
