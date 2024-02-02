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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.mostridatasca.R
import com.example.mostridatasca.model.VirtualObject
import com.example.mostridatasca.ui.ImageFromBase64
import kotlinx.coroutines.launch


@Composable
fun NearbyObjectsScreen(
    viewModel: NearbyObjectsViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (uiState.selectedObject != null) {
        ObjectScreen(
            virtualObject = uiState.selectedObject,
            selectObject = { viewModel.selectObject(it) },
            activeObject = {
                coroutineScope.launch {
                    viewModel.activeObject()
                }
            },
            activable = viewModel.isNear(uiState.selectedObject!!),
            modifier = modifier
        )
    } else {
        LazyColumn(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
            items(uiState.objects.size) { index ->
                val nearbyObject = uiState.objects[index]
                ObjectListItem(
                    virtualObject = nearbyObject,
                    near = viewModel.isNear(nearbyObject),
                    selectObject = { viewModel.selectObject(it) }
                )
                Divider()
            }
        }
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