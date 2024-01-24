package com.example.mostridatasca.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import com.example.mostridatasca.ui.theme.MostriDaTascaTheme

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileImage("name")
        UserInformation1(
            name = "name", positionShare = false
        )
        Divider()
        UserInformation2(lifePoints = "100", experience = "100")
        Divider()
    }
}

@Composable
fun ProfileImage(
    name: String,
    image: String = stringResource(id = R.string.default_image)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.padding(10.dp)
        )
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
fun UserInformation1(
    name: String,
    positionShare: Boolean
) {
    Column {
        ListItem(
            leadingContent = { Icon(Icons.Default.Face, contentDescription = null) },
            headlineContent = {
                OutlinedTextField(
                    value = name,
                    label = { Text("Edit name") },
                    onValueChange = { }
                )
            },
            trailingContent = {
                IconButton(onClick = { /*TODO*/ }, enabled = true) {
                    Icon(Icons.Default.Done, contentDescription = null)
                }
            }
        )
        ListItem(
            leadingContent = { Icon(Icons.Default.Place, contentDescription = null) },
            headlineContent = { Text("Position share") },
            trailingContent = { Switch(checked = positionShare, onCheckedChange = { TODO() }) }
        )
    }
}

@Composable
fun UserInformation2(
    lifePoints: String,
    experience: String
) {
    Column {
        ListItem(
            leadingContent = { Icon(Icons.Default.Favorite, contentDescription = null) },
            headlineContent = { Text("Life points") },
            trailingContent = { Text(experience, style = MaterialTheme.typography.bodyLarge) }
        )
        ListItem(
            leadingContent = { Icon(Icons.Default.Star, contentDescription = null) },
            headlineContent = { Text("Experience") },
            trailingContent = { Text(experience, style = MaterialTheme.typography.bodyLarge) }
        )
    }
}

@Composable
fun SingleArtifact(
    name: String,
    image: String = stringResource(id = R.string.default_image),
    type: String,
    level: String
) {
    ElevatedCard {
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)
        )
        ImageFromBase64(
            image = image,
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )
        ListItem(
            leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
            headlineContent = { Text("Type") },
            trailingContent = { SuggestionChip(onClick = { /*TODO*/ }, label = { Text(type) }) }
        )
        ListItem(
            leadingContent = { Icon(Icons.Default.Star, contentDescription = null) },
            headlineContent = { Text("Level") },
            trailingContent = { SuggestionChip(onClick = { /*TODO*/ }, label = { Text(level) }) }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun MyPreview() {
    MostriDaTascaTheme {
        ProfileScreen()
    }
}