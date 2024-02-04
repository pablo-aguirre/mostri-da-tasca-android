package com.example.mostridatasca.ui.objects

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import com.example.mostridatasca.model.VirtualObject
import com.example.mostridatasca.ui.ImageFromBase64


@Composable
fun NearbyObjectsScreen(
    viewModel: NearbyObjectsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    if (uiState.selectedObject != null) {
        ObjectScreen(
            virtualObject = uiState.selectedObject,
            selectObject = { viewModel.selectObject(it) },
            activeObject = { viewModel.activeObject() },
            activable = viewModel.isNear(uiState.selectedObject!!),
            modifier = modifier
        )
    } else {
        LazyColumn(state = listState, modifier = modifier) {
            items(uiState.objects) { virtualObject ->
                ObjectListItem(
                    virtualObject = virtualObject,
                    near = viewModel.isNear(virtualObject),
                    selectObject = { viewModel.selectObject(it) }
                )
                Divider()
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
fun ObjectListItem(
    virtualObject: VirtualObject,
    near: Boolean,
    selectObject: (VirtualObject) -> Unit
) {
    ListItem(
        leadingContent = {
            ImageFromBase64(
                image = virtualObject.image ?: defaultImage(virtualObject.type),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        },
        headlineContent = { Text(virtualObject.name) },
        supportingContent = {
            SuggestionChip(
                onClick = { selectObject(virtualObject) },
                label = { Text(if (near) "Can be activated" else "Too far away") },
                enabled = near
            )
        },
        trailingContent = {
            IconButton(onClick = { selectObject(virtualObject) }) {
                Icon(Icons.Outlined.Info, contentDescription = null)
            }
        }
    )
}

@Composable
fun defaultImage(type: String): String {
    return when (type) {
        "weapon" -> stringResource(R.string.default_weapon_image)
        "armor" -> stringResource(R.string.default_armor_image)
        "amulet" -> stringResource(R.string.default_amulet_image)
        "monster" -> stringResource(R.string.default_monster_image)
        "candy" -> stringResource(R.string.default_candy_image)
        else -> ""
    }
}