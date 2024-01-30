package com.example.mostridatasca

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Face
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Star
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mostridatasca.ui.leaderboard.LeaderBoardScreen
import com.example.mostridatasca.ui.leaderboard.LeaderBoardViewModel
import com.example.mostridatasca.ui.map.MapScreen
import com.example.mostridatasca.ui.objects.NearbyObjectsScreen
import com.example.mostridatasca.ui.profile.ProfileScreen
import com.example.mostridatasca.ui.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
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
                navigateToDestination = {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
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
                LeaderBoardScreen(
                    viewModel = viewModel(factory = LeaderBoardViewModel.Factory),
                    modifier = Modifier.padding(innerPadding)
                )
            }
            composable(route = Screens.ProfileScreen.name) {
                val context = LocalContext.current

                ProfileScreen(
                    context = context,
                    modifier = Modifier.padding(innerPadding),
                    viewModel = viewModel(factory = ProfileViewModel.Factory)
                )
            }
        }
    }
}

enum class Screens(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
) {
    MapScreen(
        title = "Map",
        selectedIcon = Icons.Filled.Place,
        unselectedIcon = Icons.Outlined.Place
    ),
    NearbyObjectsScreen(
        title = "Objects",
        selectedIcon = Icons.Filled.List,
        unselectedIcon = Icons.Outlined.List
    ),
    LeaderBoardScreen(
        title = "Leaderboard",
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.Star
    ),
    ProfileScreen(
        title = "Profile",
        selectedIcon = Icons.Filled.Face,
        unselectedIcon = Icons.Outlined.Face,
    )
}

@Composable
fun MyNavigationBar(navigateToDestination: (String) -> Unit, currentScreen: Screens) {
    NavigationBar {
        Screens.values().forEach {
            NavigationBarItem(
                selected = currentScreen == it,
                onClick = { navigateToDestination(it.name) },
                icon = {
                    if (currentScreen == it)
                        Icon(it.selectedIcon, contentDescription = null)
                    else
                        Icon(it.unselectedIcon, contentDescription = null)
                },
                label = { Text(it.title) }
            )
        }
    }
}