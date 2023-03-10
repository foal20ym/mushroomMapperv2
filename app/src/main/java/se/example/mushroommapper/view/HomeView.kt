package se.example.mushroommapper.view

import android.content.ContentValues
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import se.example.mushroommapper.model.Places
import se.example.mushroommapper.ui.theme.BACKGROUND_COLOR
import se.example.mushroommapper.ui.theme.INTERACTABLE_COLOR
import se.example.mushroommapper.viewModel.color


@Composable
fun HomeView() {

    val db = Firebase.firestore
    val placesList = ArrayList<Places>()
    var iteratorIndex = 0
    db.collection("places")
        .get()
        .addOnSuccessListener {docs ->
            for(doc in docs){
                placesList.add(doc.toObject(Places::class.java))
                Log.d(ContentValues.TAG, placesList[iteratorIndex].title)
                Log.d(ContentValues.TAG, "${placesList.size}")
                iteratorIndex += 1
            }
        }
        .addOnFailureListener{ exception ->
            Log.w(ContentValues.TAG, "Error getting documents: ", exception)
        }
    if(placesList.isNotEmpty()){
        Column(){
            Text(
                text = "There is stuff",
                color = INTERACTABLE_COLOR.color
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BACKGROUND_COLOR.color),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(top = 50.dp)
                .width(250.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            if(placesList.isEmpty()){
                Text(
                    text = "There is no stuff",
                    color = INTERACTABLE_COLOR.color
                )
            }
            for(place in placesList) {
                Text(
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .clickable {

                        },
                    text = "${place.title}",
                    textAlign = TextAlign.Center,
                    color = INTERACTABLE_COLOR.color
                )
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        },
                    color = INTERACTABLE_COLOR.color,
                    thickness = 1.dp
                )
            }
        }
    }
}