package com.example.frontened.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun ExpandableSearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    var isSearchOpen by remember { mutableStateOf(false) }


    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSearchOpen) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search doctor...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, null)
                },
                trailingIcon = {
                    IconButton(onClick = {
                        isSearchOpen = false
                        onQueryChange("")
                    }) {
                        Icon(Icons.Default.Close, null)
                    }
                },
                singleLine = true
            )
        } else {
            IconButton(onClick = { isSearchOpen = true }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }
    }
}
