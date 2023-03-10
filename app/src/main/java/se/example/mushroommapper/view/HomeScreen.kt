@file:JvmName("HomeKt")

package se.example.mushroommapper.view

import android.content.ContentValues.TAG
import androidx.compose.foundation.layout.*

import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.*
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import se.example.mushroommapper.BottomBarScreen
import se.example.mushroommapper.navigation.HomeNavGraph
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.navigation.Graph
import se.example.mushroommapper.viewModel.HomeViewModel
import se.example.mushroommapper.viewModel.MapViewModel
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import java.io.File
import java.util.concurrent.ExecutorService
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import se.example.mushroommapper.R
import se.example.mushroommapper.model.Places
import java.util.concurrent.Executors
import se.example.mushroommapper.ui.theme.BACKGROUND_COLOR
import se.example.mushroommapper.ui.theme.INTERACTABLE_COLOR
import se.example.mushroommapper.ui.theme.NON_INTERACTABLE_COLOR
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
        topBar = { TopBar(navController = navController)},
        modifier = Modifier
    ) { PaddingValues ->
        Column(modifier = Modifier.padding(PaddingValues)){
            HomeNavGraph(navController = navController, homeViewModel = homeViewModel, detailViewModel = detailViewModel)
        }
    }

}

@Composable
fun TopBar(navController: NavHostController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()){
                Text(text = "${currentDestination?.route}", modifier = Modifier.align(Alignment.Center))
            }
        },
        navigationIcon = {
            IconButton(onClick = {navController.navigate(Graph.HOME)}) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        actions = {
            IconButton(onClick = {
                navController.navigate("CameraScreen")
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