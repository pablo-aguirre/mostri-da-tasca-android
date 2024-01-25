package com.example.mostridatasca

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class Screen(title: String) {
    MapScreen(title = "Map"),
    ObjectsScreen(title = "Nearby Objects"),
    LeaderBoardScreen(title = "Leaderboard"),
    ProfileScreen(title = "Profile")
}

@Composable
fun MostriDaTasca(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            MyNavigationBar(onButtonClicked = { navController.navigate(it) })
        }
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = Screen.MapScreen.name
        ) {
            composable(route = Screen.MapScreen.name) {
                Text("Map")
            }
            composable(route = Screen.ObjectsScreen.name) {
                Text("Nearby objects")
            }
            composable(route = Screen.LeaderBoardScreen.name) {
                Text("Leaderboard")
            }
            composable(route = Screen.ProfileScreen.name) {
                Text("Profile")
            }
        }
    }
}

@Composable
fun MyNavigationBar(
    onButtonClicked: (String) -> Unit,
) {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = { onButtonClicked(Screen.MapScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.map), contentDescription = null) },
            label = { Text("Map") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onButtonClicked(Screen.ObjectsScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.view_list), contentDescription = null) },
            label = { Text("Objects") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onButtonClicked(Screen.LeaderBoardScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.trophy), contentDescription = null) },
            label = { Text("Leaderboard") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onButtonClicked(Screen.ProfileScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.account), contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}