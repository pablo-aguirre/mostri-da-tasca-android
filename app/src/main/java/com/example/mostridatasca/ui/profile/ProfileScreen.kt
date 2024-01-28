package com.example.mostridatasca.ui.profile

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mostridatasca.R
import com.example.mostridatasca.ui.ImageFromBase64

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    context: Context
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.error -> Text(text = "Error", modifier = modifier)
        uiState.loading -> Text(text = "Loading", modifier = modifier)
        else ->
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.name,
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(10.dp)
                )
                ProfileImage(
                    image = uiState.picture ?: stringResource(id = R.string.default_user_image),
                    updatePicture = { viewModel.updatePicture(context.contentResolver, it) }
                )
                UserInformation1(
                    newName = uiState.newName,
                    positionShare = uiState.positionShare,
                    buttonEnabled = uiState.isNewNameValid,
                    setNewName = { viewModel.setNewName(it) },
                    updateName = { viewModel.updateName(it) },
                    updatePositionShare = { viewModel.updatePositionShare(it) }
                )
                Divider()
                UserInformation2(lifePoints = uiState.life, experience = uiState.experience)
                Divider()
            }
    }
}

@Composable
fun ProfileImage(
    image: String,
    updatePicture: (Uri?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { updatePicture(it) }
    )

    Box {
        ImageFromBase64(
            image = image,
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
        IconButton(
            onClick = { launcher.launch("image/*") },
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Outlined.Edit, contentDescription = null)
        }
    }
}

@Composable
fun UserInformation1(
    newName: String,
    positionShare: Boolean,
    buttonEnabled: Boolean,
    setNewName: (String) -> Unit,
    updateName: (String) -> Unit,
    updatePositionShare: (Boolean) -> Unit,
) {
    Column {
        ListItem(
            leadingContent = { Icon(Icons.Default.Face, contentDescription = null) },
            headlineContent = {
                OutlinedTextField(
                    value = newName,
                    label = { Text("Edit name") },
                    onValueChange = { setNewName(it) }
                )
            },
            trailingContent = {
                IconButton(onClick = { updateName(newName) }, enabled = buttonEnabled) {
                    Icon(Icons.Default.Done, contentDescription = null)
                }
            }
        )
        ListItem(
            leadingContent = { Icon(Icons.Default.Place, contentDescription = null) },
            headlineContent = { Text("Position share") },
            trailingContent = {
                Switch(
                    checked = positionShare,
                    onCheckedChange = { updatePositionShare(it) })
            }
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
            trailingContent = { Text(lifePoints, style = MaterialTheme.typography.bodyLarge) }
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
    image: String = stringResource(id = R.string.default_user_image),
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
            trailingContent = {
                SuggestionChip(
                    onClick = { /*TODO*/ },
                    label = { Text(level) })
            }
        )
    }
}