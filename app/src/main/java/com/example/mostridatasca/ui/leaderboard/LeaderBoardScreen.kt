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
import com.example.mostridatasca.model.User
import com.example.mostridatasca.ui.ImageFromBase64

@Composable
fun LeaderBoardScreen(
    viewModel: LeaderBoardViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.selectedUser != null) {
        UserScreen(
            user = uiState.selectedUser,
            selectUser = { viewModel.selectUser(it) },
            modifier = modifier
        )
    } else {
        LazyColumn(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
            items(uiState.users.size) { index ->
                val user = uiState.users[index]
                UserListItem(
                    user = user,
                    selectUser = { viewModel.selectUser(it) }
                )
                Divider()
            }
        }
    }
}

@Composable
fun UserListItem(
    user: User,
    selectUser: (User) -> Unit
) {
    ListItem(
        leadingContent = {
            ImageFromBase64(
                image = user.picture
                    ?: stringResource(id = com.example.mostridatasca.R.string.default_user_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        },
        headlineContent = { Text(user.name) },
        supportingContent = { Text("${user.experience} XP") },
        trailingContent = {
            IconButton(onClick = { selectUser(user) }) {
                Icon(Icons.Outlined.Info, contentDescription = null)
            }
        }
    )
}