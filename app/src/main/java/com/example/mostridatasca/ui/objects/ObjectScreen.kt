package com.example.mostridatasca.ui.objects

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.mostridatasca.model.VirtualObject
import com.example.mostridatasca.ui.ImageFromBase64

@Composable
fun ObjectScreen(
    virtualObject: VirtualObject?,
    selectObject: (VirtualObject?) -> Unit,
    activeObject: () -> Unit,
    modifier: Modifier = Modifier
) {
    var confirmationRequired by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { selectObject(null) },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = null)
            }
            Column(
                modifier = Modifier.align(Alignment.Center),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = virtualObject?.name ?: "No object selected",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                ImageFromBase64(
                    image = virtualObject?.image ?: "",
                    modifier = Modifier
                        .size(180.dp)
                        .padding(10.dp)
                        .clip(CircleShape)
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Divider()
        ObjectInformation(
            type = virtualObject?.type.toString(),
            level = virtualObject?.level.toString()
        )
        Divider()
        Button(
            onClick = { confirmationRequired = true },
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.End)
        ) {
            Text(text = customText(virtualObject!!.type))
        }
        if (confirmationRequired) {
            ConfirmationDialog(
                onConfirm = {
                    confirmationRequired = false
                    activeObject()
                },
                onCancel = { confirmationRequired = false },
                virtualObject = virtualObject!!
            )
        }
    }
}

@Composable
fun ObjectInformation(
    type: String,
    level: String
) {
    Column {
        ListItem(
            leadingContent = { Icon(Icons.Default.Info, contentDescription = null) },
            headlineContent = { Text("Type") },
            trailingContent = { Text(type, style = MaterialTheme.typography.bodyLarge) }
        )
        ListItem(
            leadingContent = { Icon(Icons.Default.Star, contentDescription = null) },
            headlineContent = { Text("Level") },
            trailingContent = { Text(level, style = MaterialTheme.typography.bodyLarge) }
        )
    }
}

@Composable
fun ConfirmationDialog(
    virtualObject: VirtualObject,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(onDismissRequest = { /* Do nothing */ },
        title = { Text("Attention") },
        text = { Text(customText2(virtualObject)) },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(customText(virtualObject.type))
            }
        }
    )
}

fun customText(type: String): String {
    return when (type) {
        "monster" -> "Attack"
        "candy" -> "Eat"
        else -> "Equip"
    }
}

fun customText2(virtualObject: VirtualObject): String {
    return when (virtualObject.type) {
        "monster" -> "You could loose life and gain experience in a range of ${virtualObject.level} to ${virtualObject.level * 2}.\n If you die, you will lose experience, equipment and your life will be reset to 100."
        "candy" -> "You will increase your life in a range of ${virtualObject.level} to ${virtualObject.level * 2}."
        else -> "You will substitute your current ${virtualObject.type} with this one."
    }
}
