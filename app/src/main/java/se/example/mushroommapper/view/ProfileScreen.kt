package se.example.mushroommapper.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import se.example.mushroommapper.firestore.addDataToFireStore

@Composable
fun ProfileScreen(){
    Row() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val title = remember { mutableStateOf("") }
            val description = remember { mutableStateOf("") }
            val latitude = remember { mutableStateOf("") }
            val longitude = remember { mutableStateOf("") }
            val context = LocalContext.current

            Text(
                text = "Mushroom Mapper",
                style = TextStyle(fontSize = 40.sp)
            )

            Icon(Icons.Default.Star, contentDescription = "Star Icon")

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Add a place to your map!",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Default
                )
            )

            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Title") },
                value = title.value,
                onValueChange = { title.value = it }
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Description") },
                value = description.value,
                onValueChange = { description.value = it }
            )
            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                label = { Text(text = "Latitude") },
                value = latitude.value.toString(),
                onValueChange = { value -> latitude.value = value }
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextField(
                label = { Text(text = "Longitude") },
                value = longitude.value.toString(),
                onValueChange = { value -> longitude.value = value }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = { addDataToFireStore(title.value, description.value, (latitude.value).toDouble(), (longitude.value).toDouble(), context) },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Add data")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}