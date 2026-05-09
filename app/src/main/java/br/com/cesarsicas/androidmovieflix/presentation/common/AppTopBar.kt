package br.com.cesarsicas.androidmovieflix.presentation.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

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
    var query by remember { mutableStateOf("") }

    TopAppBar(
        title = {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = { if (query.isNotBlank()) onSearchSubmit(query.trim()) },
                ),
                trailingIcon = {
                    IconButton(onClick = { if (query.isNotBlank()) onSearchSubmit(query.trim()) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
            )
        },
        navigationIcon = {
            TextButton(onClick = onLogoClick) { Text("ReactFlix") }
        },
        actions = {
            if (onBrowseClick != null) {
                TextButton(onClick = onBrowseClick) { Text("Browse") }
            }
            TextButton(onClick = onWatchPartyClick) { Text("Watch Party") }
            TextButton(onClick = onAuthClick) {
                Text(if (isLoggedIn) "Profile" else "Login")
            }
        },
        modifier = modifier,
    )
}
