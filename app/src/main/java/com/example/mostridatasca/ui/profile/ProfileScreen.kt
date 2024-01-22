package com.example.mostridatasca.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mostridatasca.R
import com.example.mostridatasca.ui.ImageFromBase64

@Composable
fun UserImage(
    image: String = stringResource(id = R.string.default_image)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageFromBase64(
            image = image,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
        )
        IconButton(onClick = { /*TODO*/ }) {
            Icon(Icons.Outlined.Edit, contentDescription = null)
        }
    }
}

@Composable
fun UserInformation(
    name: String,
    lifePoints: String,
    experience: String
) {
    Column {
        ListItem(
            leadingContent = { Icon(Icons.Default.Face, contentDescription = null) },
            headlineContent = {
                TextField(
                    value = name,
                    onValueChange = { TODO() }
                )
            },
            trailingContent = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                }
            }
        )
        ListItem(
            leadingContent = { Icon(Icons.Default.Place, contentDescription = null) },
            headlineContent = { Text("Position share") },
            trailingContent = { Switch(checked = true, onCheckedChange = { TODO() }) }
        )
        Divider()
        ListItem(
            leadingContent = { Icon(Icons.Default.Favorite, contentDescription = null) },
            headlineContent = { Text("Life points") },
            trailingContent = {
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text(lifePoints) })
            }
        )
        ListItem(
            leadingContent = { Icon(Icons.Default.Star, contentDescription = null) },
            headlineContent = { Text("Experience") },
            trailingContent = {
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text(experience) })
            }
        )
        Divider()
    }
}

@Preview
@Composable
fun MyPreview() {
    UserInformation(
        name = "Pablo",
        lifePoints = "100",
        experience = "50"
    )
}