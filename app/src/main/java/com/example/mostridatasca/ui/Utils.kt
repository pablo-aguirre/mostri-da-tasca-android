package com.example.mostridatasca.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.mostridatasca.R
import com.example.mostridatasca.ui.theme.MostriDaTascaTheme


@Preview
@Composable
fun MyNavigationBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(painterResource(id = R.drawable.map), contentDescription = null) }
        )
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(painterResource(id = R.drawable.view_list), contentDescription = null) }
        )
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(painterResource(id = R.drawable.trophy), contentDescription = null) }
        )
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(painterResource(id = R.drawable.account), contentDescription = null) }
        )
    }
}