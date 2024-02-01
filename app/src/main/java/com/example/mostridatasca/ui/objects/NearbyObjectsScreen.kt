package com.example.mostridatasca.ui.objects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mostridatasca.R
import com.example.mostridatasca.ui.ImageFromBase64
import com.example.mostridatasca.ui.leaderboard.UserListItem


@Composable
fun NearbyObjectsScreen(
    viewModel: NearbyObjectsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    LazyColumn(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        items(uiState.objects.size) { index ->
            val nearbyObject = uiState.objects[index]
            ObjectListItem(
                image = nearbyObject.image ?: stringResource(id = R.string.default_user_image),
                name = nearbyObject.name,
                near = false,
                onButtonClick = { /*TODO*/ }
            )
            Divider()
        }
    }
}

@Composable
fun ObjectListItem(
    image: String,
    name: String,
    near: Boolean,
    onButtonClick: () -> Unit
) {
    ListItem(
        leadingContent = {
            ImageFromBase64(
                image = image,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        },
        headlineContent = { Text(name) },
        supportingContent = {
            if (near) {
                SuggestionChip(onClick = { /*TODO*/ }, label = { Text("Can be activated") })
            }
        },
        trailingContent = {
            IconButton(onClick = onButtonClick) {
                Icon(Icons.Outlined.Info, contentDescription = null)
            }
        }
    )
}

