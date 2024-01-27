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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mostridatasca.ui.leaderboard.LeaderBoardScreen
import com.example.mostridatasca.ui.map.MapScreen
import com.example.mostridatasca.ui.objects.NearbyObjectsScreen
import com.example.mostridatasca.ui.profile.ProfileScreen

enum class Screens(val title: String) {
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
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screens.valueOf(
        backStackEntry?.destination?.route ?: Screens.MapScreen.name
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text(currentScreen.title) }) },
        bottomBar = {
            MyNavigationBar(
                currentScreen = currentScreen,
                onButtonClicked = { navController.navigate(it) })
        }
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
                val context = LocalContext.current
                ProfileScreen(context = context, modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun MyNavigationBar(onButtonClicked: (String) -> Unit, currentScreen: Screens) {
    NavigationBar {
        NavigationBarItem(
            selected = currentScreen == Screens.MapScreen,
            onClick = { onButtonClicked(Screens.MapScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.map), contentDescription = null) },
            label = { Text("Map") }
        )
        NavigationBarItem(
            selected = currentScreen == Screens.NearbyObjectsScreen,
            onClick = { onButtonClicked(Screens.NearbyObjectsScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.view_list), contentDescription = null) },
            label = { Text("Objects") }
        )
        NavigationBarItem(
            selected = currentScreen == Screens.LeaderBoardScreen,
            onClick = { onButtonClicked(Screens.LeaderBoardScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.trophy), contentDescription = null) },
            label = { Text("Leaderboard") }
        )
        NavigationBarItem(
            selected = currentScreen == Screens.ProfileScreen,
            onClick = { onButtonClicked(Screens.ProfileScreen.name) },
            icon = { Icon(painterResource(id = R.drawable.account), contentDescription = null) },
            label = { Text("Profile") }
        )
    }
}