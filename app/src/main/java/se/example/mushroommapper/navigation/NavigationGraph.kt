package se.example.mushroommapper.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import se.example.mushroommapper.view.ResetPasswordScreen
import se.example.mushroommapper.view.SignInScreen
import se.example.mushroommapper.view.SignUpScreen


fun NavGraphBuilder.NavigationGraph(
    navController: NavHostController,
) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = Screens.SignInScreen.route
    ) {
        composable(route = Screens.SignInScreen.route) {
            SignInScreen(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)
                },
                onSignUpClick = {
                    navController.navigate(Screens.SignUpScreen.route)
                },
                onForgotClick = {
                    navController.navigate(Screens.ResetPasswordScreen.route)
                }
            )
        }

        composable(route = Screens.SignUpScreen.route) {
            SignUpScreen(
                onClick = {
                    navController.navigate(Screens.SignInScreen.route)
                }
            )
        }

        composable(route = Screens.ResetPasswordScreen.route) {
            ResetPasswordScreen(
                onClick = {
                    navController.navigate(Screens.SignInScreen.route)
                },
                onSignUpClick = {
                    navController.navigate(Screens.SignUpScreen.route)
                }
            )
        }
    }

}