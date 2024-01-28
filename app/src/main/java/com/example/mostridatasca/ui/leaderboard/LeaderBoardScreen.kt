package com.example.mostridatasca.ui.leaderboard

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mostridatasca.ui.ImageFromBase64

@Composable
fun LeaderBoardScreen(
    viewModel: LeaderBoardViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        items(uiState.users.size) { index ->
            val user = uiState.users[index]
            UserListItem(
                image = user.picture,
                name = user.name,
                xp = user.experience.toString(),
                onButtonClick = {}
            )
            Divider()
        }
    }
}

@Composable
fun UserListItem(
    image: String?,
    name: String,
    xp: String,
    onButtonClick: () -> Unit
) {
    ListItem(
        leadingContent = {
            ImageFromBase64(
                image = image
                    ?: stringResource(id = com.example.mostridatasca.R.string.default_user_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        },
        headlineContent = { Text(name) },
        supportingContent = { Text("$xp XP") },
        trailingContent = {
            IconButton(onClick = onButtonClick) {
                Icon(Icons.Outlined.Info, contentDescription = null)
            }
        }
    )
}