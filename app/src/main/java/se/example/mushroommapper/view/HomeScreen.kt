@file:JvmName("HomeKt")

package se.example.mushroommapper.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.navigation.Graph
import se.example.mushroommapper.navigation.HomeNavGraph
import se.example.mushroommapper.ui.theme.INTERACTABLE_COLOR
import se.example.mushroommapper.viewModel.HomeViewModel
import se.example.mushroommapper.viewModel.MapViewModel
import se.example.mushroommapper.viewModel.color

@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel,
    mapViewModel: MapViewModel
) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController) },
        topBar = { TopBar(navController = navController, detailViewModel)},
        modifier = Modifier
    ) { PaddingValues ->
        Column(modifier = Modifier.padding(PaddingValues)){
            HomeNavGraph(navController = navController, homeViewModel = homeViewModel, detailViewModel = detailViewModel)
        }
    }

}

@Composable
fun TopBar(navController: NavHostController,detailViewModel: DetailViewModel) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()){
                Text(text = "${currentDestination?.route}", modifier = Modifier.align(Alignment.Center))
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                navController.navigate(Graph.HOME)
                detailViewModel.resetState()
            }) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },

        actions = {
            IconButton(onClick = {
                navController.navigate("ManuallyAddLocationScreen")
            }) {
                Icon(Icons.Default.Add, "AddIcon")
            }
            IconButton(onClick = {
                navController.navigate(Graph.CAMERA)
            }) {
                Icon(Icons.Filled.PhotoCamera, "cameraIcon")
            }
        },
        backgroundColor = INTERACTABLE_COLOR.color,
        contentColor = Color.White,
        elevation = 10.dp
    )
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Profile,
        BottomBarScreen.Map,
        BottomBarScreen.Settings,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomNavigation {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}