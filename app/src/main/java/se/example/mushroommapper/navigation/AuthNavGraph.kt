package se.example.mushroommapper.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import se.example.mushroommapper.view.HomeScreen

import se.example.mushroommapper.view.ResetPasswordScreen
import se.example.mushroommapper.view.SignInScreen
import se.example.mushroommapper.view.SignUpScreen

fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
            SignInScreen(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Graph.HOME)
                },
                onSignUpClick = {
                    navController.navigate(AuthScreen.SignUp.route)
                },
                onForgotClick = {
                    navController.navigate(AuthScreen.Reset.route)
                }
            )
        }

        composable(route = AuthScreen.SignUp.route) {
            SignUpScreen(
                onClick = {
                navController.popBackStack()
                navController.navigate(Screens.SignInScreen.route)
                },
               )
        }
        composable(route = AuthScreen.Reset.route) {
            ResetPasswordScreen(
                onClick = {
                    navController.popBackStack()
                    navController.navigate(Screens.SignInScreen.route)
                },
                onSignUpClick = {
                    navController.navigate(AuthScreen.SignUp.route)
                }
            )
        }
    }
}

sealed class Screens(val route: String) {
    object SignInScreen : Screens(route = "SignIn_Screen")
    object SignUpScreen : Screens(route = "SignUp_Screen")
    object ResetPasswordScreen : Screens(route = "ResetPassword_Screen")
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen(route = "LOGIN")
    object SignUp : AuthScreen(route = "SIGN_UP")
    object Reset : AuthScreen(route = "RESET")
}
