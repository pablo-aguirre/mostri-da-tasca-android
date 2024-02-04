package com.example.mostridatasca.ui.leaderboard

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
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
    val listState = rememberLazyListState()

    if (uiState.selectedUser != null) {
        UserScreen(
            user = uiState.selectedUser,
            selectUser = { viewModel.selectUser(it) },
            modifier = modifier
        )
    } else {
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.updateLeaderBoard() },
                    content = {
                        Icon(Icons.Outlined.Refresh, contentDescription = "Refresh")
                    }
                )
            }
        ) {
            LazyColumn(state = listState, modifier = Modifier.padding(it)) {
                items(uiState.users) { user ->
                    UserListItem(user = user, selectUser = { viewModel.selectUser(it) })
                    Divider()
                }
            }
        }
    }
    if (uiState.errorMessage.isNotBlank()) {
        AlertDialog(
            onDismissRequest = { /* Do nothing */ },
            icon = { Icon(Icons.Default.Warning, contentDescription = null) },
            title = { Text("Attention") },
            text = { Text(uiState.errorMessage) },
            confirmButton = {
                Button(onClick = { viewModel.deleteError() }) {
                    Text("OK")
                }
            }
        )
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