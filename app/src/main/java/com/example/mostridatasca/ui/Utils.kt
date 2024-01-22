package com.example.mostridatasca.ui

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mostridatasca.R


@Preview
@Composable
fun MyNavigationBar(modifier: Modifier = Modifier) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(painterResource(id = R.drawable.map), contentDescription = null) },
            label = { Text("Map") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(painterResource(id = R.drawable.view_list), contentDescription = null) },
            label = { Text("Objects") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(painterResource(id = R.drawable.trophy), contentDescription = null) },
            label = { Text("Leaderboard") }
        )
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(painterResource(id = R.drawable.account), contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}

@Composable
fun ImageFromBase64(image: String, modifier: Modifier = Modifier, contentScale: ContentScale = ContentScale.Fit) {
    val byteArray = Base64.decode(image, Base64.DEFAULT);
    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = null,
        modifier = modifier,
        contentScale = contentScale
    )
}