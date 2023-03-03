package se.example.mushroommapper.firestore

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import se.example.mushroommapper.model.Place

class DataViewModel: ViewModel() {
    val state = mutableStateOf(Place())

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            state.value = getDataFromFireStore()
        }
    }
}

fun addDataToFireStore(
    title: String,
    description: String,
    latitude: Double,
    longitude: Double,
    context: Context
) {
    val db = FirebaseFirestore.getInstance()
    val dbAbout: CollectionReference = db.collection("Place")
    val place = Place(title, description, latitude, longitude)

    dbAbout.add(place).addOnSuccessListener {
        Toast.makeText(
            context,
            "Your Place has been added to Firebase Firestore",
            Toast.LENGTH_SHORT
        ).show()
    }.addOnFailureListener { e ->
        // this method is called when the data addition process is failed.
        // displaying a toast message when data addition is failed.
        Toast.makeText(context, "Fail to add about \n$e", Toast.LENGTH_SHORT).show()
    }

}

suspend fun getDataFromFireStore(): Place {
    val db = FirebaseFirestore.getInstance()
    var place = Place()

    try {
        db.collection("place").get().await().map {
            val result = it.toObject(Place::class.java)
            place = result
        }
    }catch (e: FirebaseFirestoreException) {
        Log.d("error", "getDataFromFireStore: $e")
    }
    return place
}

/*
fun addDataToFireStore(
    name: String,
    age: String,
    passion: String,
    photo: String,
    context: Context
) {
    val db = FirebaseFirestore.getInstance()
    val dbAbout: CollectionReference = db.collection("About")
    val about = About(name, age, passion, photo)

    dbAbout.add(about).addOnSuccessListener {
        Toast.makeText(
            context,
            "Your About has been added to Firebase Firestore",
            Toast.LENGTH_SHORT
        ).show()
    }.addOnFailureListener { e ->
        // this method is called when the data addition process is failed.
        // displaying a toast message when data addition is failed.
        Toast.makeText(context, "Fail to add about \n$e", Toast.LENGTH_SHORT).show()
    }

}
*/

/*
suspend fun getDataFromFireStore():About{
    val db = FirebaseFirestore.getInstance()
    var about = About()

    try {
        db.collection("about").get().await().map {
            val result = it.toObject(About::class.java)
            about = result
        }
    }catch (e: FirebaseFirestoreException) {
        Log.d("error", "getDataFromFireStore: $e")
    }
    return about
}
*/
