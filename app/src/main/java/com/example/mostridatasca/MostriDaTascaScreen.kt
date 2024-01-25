package com.example.mostridatasca

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mostridatasca.ui.leaderboard.LeaderBoardScreen
import com.example.mostridatasca.ui.map.MapScreen
import com.example.mostridatasca.ui.objects.NearbyObjectsScreen
import com.example.mostridatasca.ui.profile.ProfileScreen

enum class Screens(title: String) {
    MapScreen(title = "Map"),
    NearbyObjectsScreen(title = "Nearby Objects"),
    LeaderBoardScreen(title = "Leaderboard"),
    ProfileScreen(title = "Profile")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun MostriDaTasca(
    navController: NavHostController = rememberNavController()
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("TODO") }) },
        bottomBar = { MyNavigationBar(onButtonClicked = { navController.navigate(it) }) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screens.MapScreen.name
        ) {
            composable(route = Screens.MapScreen.name) {
                MapScreen(modifier = Modifier.padding(innerPadding))
            }
            composable(route = Screens.NearbyObjectsScreen.name) {
                NearbyObjectsScreen(modifier = Modifier.padding(innerPadding))
            }
            composable(route = Screens.LeaderBoardScreen.name) {
                LeaderBoardScreen(modifier = Modifier.padding(innerPadding))
            }
            composable(route = Screens.ProfileScreen.name) {
                ProfileScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun MyNavigationBar(onButtonClicked: (String) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { onButtonClicked(Screens.MapScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.map), contentDescription = null) },
            label = { Text("Map") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onButtonClicked(Screens.NearbyObjectsScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.view_list), contentDescription = null) },
            label = { Text("Objects") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onButtonClicked(Screens.LeaderBoardScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.trophy), contentDescription = null) },
            label = { Text("Leaderboard") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { onButtonClicked(Screens.ProfileScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.account), contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}