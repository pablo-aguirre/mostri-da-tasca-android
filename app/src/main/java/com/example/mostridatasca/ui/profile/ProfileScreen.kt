package com.example.mostridatasca.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

@Preview
@Composable
fun MyPreview() {
}