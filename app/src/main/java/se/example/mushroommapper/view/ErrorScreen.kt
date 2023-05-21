package se.example.mushroommapper.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

val errorMessages = mutableListOf<String>()

@Composable
fun ErrorDialog(errorMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Error") },
        text = { Text(text = errorMessage) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = "Dismiss")
            }
        }
    )
}


@Composable
fun ErrorScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .width(150.dp).height(50.dp),
        verticalArrangement = Arrangement.Center

    ) {
        for (error in errorMessages) {
            Text(
                text = error, color = Color.Red, modifier = Modifier.padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
        Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Button(onClick = {
                navController.navigateUp()
                errorMessages.clear()
            }) {
                Text("OK")
            }
        }
    }
}