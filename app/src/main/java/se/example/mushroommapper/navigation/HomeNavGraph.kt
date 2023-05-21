package se.example.mushroommapper.navigation

import Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import se.example.mushroommapper.BottomBarScreen
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.view.*
import se.example.mushroommapper.viewModel.HomeViewModel

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    detailViewModel: DetailViewModel
) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Home.route,
        modifier = modifier
    ) {

        composable(route = BottomBarScreen.Home.route){
            Home(
                homeViewModel = homeViewModel,
                onPlaceClick = { noteId ->
                    navController.navigate(
                        BottomBarScreen.Profile.route + "?id=$noteId"
                    ){
                        launchSingleTop = true
                    }
                },
                navToDetailPage = {
                    navController.navigate(BottomBarScreen.Profile.route)
                }
            ) {
                navController.navigate(Graph.AUTHENTICATION){
                    launchSingleTop = true
                    popUpTo(0){
                        inclusive = true
                    }
                }
            }
        }
        // 1:28:32
        composable(
            route = BottomBarScreen.Profile.route + "?id={id}",
            arguments = listOf(navArgument("id"){
                type = NavType.StringType
                defaultValue = ""
            })
        ) { entry ->
            DetailScreen(
                detailViewModel = detailViewModel,
                placeId = entry.arguments?.getString("id") as String,
            ) {
                navController.navigateUp()
            }
        }
        composable(route = BottomBarScreen.Profile.route){
            ProfileScreen(
                homeViewModel = homeViewModel,
                onPlaceClick = { placeId ->
                    navController.navigate(
                        BottomBarScreen.Profile.route + "?id=$placeId"
                    ){
                        launchSingleTop = true
                    }
                },
                navToDetailPage = {
                    navController.navigate(BottomBarScreen.Profile.route)
                }
            ) {

            }
        }
        composable(route = BottomBarScreen.Map.route) {
            MapScreen(homeViewModel = homeViewModel)
        }
        composable(route = BottomBarScreen.Settings.route) {
            ImagePicker(detailViewModel = detailViewModel)
        }
        composable(Graph.CAMERA) {
            CameraScreen(navController = navController)
        }
        composable("ManuallyAddLocationScreen") {
            ManuallyAddLocationScreen(detailViewModel = detailViewModel){
                navController.navigateUp()
            }
        }
        detailsNavGraph(navController = navController)
    }
}

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailsScreen.Information.route
    ) {
        composable(route = DetailsScreen.Information.route) {
            ScreenContent(name = DetailsScreen.Information.route) {
                navController.navigate(DetailsScreen.Overview.route)
            }
        }
        composable(route = DetailsScreen.Overview.route) {
            ScreenContent(name = DetailsScreen.Overview.route) {
                navController.popBackStack(
                    route = DetailsScreen.Information.route,
                    inclusive = false
                )
            }
        }
    }
}

sealed class DetailsScreen(val route: String) {
    object Information : DetailsScreen(route = "INFORMATION")
    object Overview : DetailsScreen(route = "OVERVIEW")
}