package se.example.mushroommapper.model

import com.google.firebase.Timestamp

data class Images(
    val imageUri: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val userId: String = "",
    val documentId: String = ""
)