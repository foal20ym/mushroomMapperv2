package se.example.mushroommapper.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

val errorMessages = mutableListOf<String>()

@Composable
fun ErrorScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
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
                navController.navigate("viewAll")
                errorMessages.clear()
            }) {
                Text("OK")
            }
        }
    }
}