package com.example.mostridatasca.ui.leaderboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mostridatasca.R
import com.example.mostridatasca.ui.ImageFromBase64

@Composable
fun LeaderBoardScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.background(MaterialTheme.colorScheme.primary)) {
        Text("LeaderBoardScreen")
    }
}

@Composable
fun UserListItem(
    image: String = stringResource(id = R.string.default_user_image),
    name: String,
    xp: String,
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
        supportingContent = { Text("$xp XP") },
        trailingContent = {
            IconButton(onClick = onButtonClick) {
                Icon(Icons.Outlined.Info, contentDescription = null)
            }
        }
    )
}

@Preview
@Composable
fun MyPreview() {
    Column {
        UserListItem(
            name = "Pablo",
            xp = "2208",
            onButtonClick = { TODO() }
        )
        UserListItem(
            name = "Pablo",
            xp = "2208",
            onButtonClick = { TODO() }
        )
    }
}