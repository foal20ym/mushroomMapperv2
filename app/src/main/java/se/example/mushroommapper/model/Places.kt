package se.example.mushroommapper.model

import com.google.firebase.Timestamp

data class Places(
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val timestamp: Timestamp = Timestamp.now(),
    val documentId: String = "",
)