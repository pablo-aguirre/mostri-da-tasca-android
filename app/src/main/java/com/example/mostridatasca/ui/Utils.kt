package com.example.mostridatasca.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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