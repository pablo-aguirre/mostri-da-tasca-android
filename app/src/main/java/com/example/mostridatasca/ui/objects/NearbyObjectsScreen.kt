package com.example.mostridatasca.ui.objects

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SuggestionChip
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
import com.example.mostridatasca.ui.theme.MostriDaTascaTheme

@Composable
fun ObjectListItem(
    image: String = stringResource(id = R.string.default_image),
    name: String,
    near: Boolean,
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
        supportingContent = {
            if (near) {
                SuggestionChip(onClick = { /*TODO*/ }, label = { Text("Can be activated") })
            }
        },
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
    MostriDaTascaTheme {
        Column {
            ObjectListItem(
                name = "Monster1",
                near = false,
                onButtonClick = { System.out.println("pablo") }
            )
            ObjectListItem(
                name = "Monster2",
                near = true,
                onButtonClick = { System.out.println("pablo") }
            )
        }
    }
}

