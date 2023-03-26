package se.example.mushroommapper.viewModel

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
import se.example.mushroommapper.model.Places

class DataViewModel: ViewModel() {
    val state = mutableStateOf(Places())

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
    val place = Places(title, description, latitude = 0.0, longitude = 0.0)

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

suspend fun getDataFromFireStore(): Places {
    val db = FirebaseFirestore.getInstance()
    var place = Places()

    try {
        db.collection("place").get().await().map {
            val result = it.toObject(Places::class.java)
            place = result
        }
    }catch (e: FirebaseFirestoreException) {
        Log.d("error", "getDataFromFireStore: $e")
    }
    return place
}