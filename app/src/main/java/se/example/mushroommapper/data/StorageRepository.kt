package se.example.mushroommapper.data

import android.net.Uri
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import se.example.mushroommapper.model.Images
import se.example.mushroommapper.model.Places
import java.util.UUID


const val NOTES_COLLECTION_REF = "notes"
const val PLACES_COLLECTION_REF = "places"
const val IMAGES_COLLECTION_REF = "images"

class StorageRepository {

    fun user() = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val notesRef: CollectionReference = Firebase.firestore.collection(NOTES_COLLECTION_REF)
    private val placeRef: CollectionReference = Firebase.firestore.collection(PLACES_COLLECTION_REF)
    private val imageRef: CollectionReference = Firebase.firestore.collection((IMAGES_COLLECTION_REF)
    )

    fun addImage(
        path: Uri,
        timeStamp: Timestamp,
        userId: String,
        onComplete: (Boolean) -> Unit
    ){
        val documentId = UUID.randomUUID().toString()
        val image = Images(path.toString(), timeStamp, userId, documentId)

        imageRef
            .document(documentId)
            .set(image)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }
    fun getUserPlaces(
        userId: String,
    ):Flow<Resources<List<Places>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = placeRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val places = snapshot.toObjects(Places::class.java)
                        Resources.Success(data = places)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }
        }catch (e:Exception){
            trySend( Resources.Error(e?.cause))
            e.printStackTrace()
        }
        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    fun getLastFiveUserPlaces(
        userId: String,
    ):Flow<Resources<List<Places>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = placeRef
                .orderBy("timestamp", Query.Direction.DESCENDING).limit(5)
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val places = snapshot.toObjects(Places::class.java)
                        Resources.Success(data = places)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend(response)
                }
        }catch (e:Exception){
            trySend( Resources.Error(e?.cause))
            e.printStackTrace()
        }
        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    fun getPlace(
        placeId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Places?) -> Unit
    ){
        placeRef
            .document(placeId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Places::class.java))
            }
            .addOnFailureListener { result ->
                onError.invoke(result.cause)
            }
    }

    fun addPlace(
        userId: String,
        title: String,
        description: String,
        latitude: Double,
        longitude: Double,
        timestamp: Timestamp,
        onComplete: (Boolean) -> Unit,
    ){
        val documentId = placeRef.document().id
        val place = Places(userId, title, description, latitude, longitude, timestamp, documentId = documentId)

        placeRef
            .document(documentId)
            .set(place)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    fun deletePlace(placeId: String, onComplete: (Boolean) -> Unit){
        placeRef.document(placeId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    fun updatePlace(
        title: String,
        description: String,
        placeId: String,
        onResult: (Boolean) -> Unit
    ){
        val updateData = hashMapOf<String, Any>(
            "description" to description,
            "title" to title
        )

        placeRef.document(placeId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }
    }

}

sealed class Resources<T>(
    val data: T? = null,
    val throwable: Throwable? = null,
){
    class Loading<T>:Resources<T>()
    class Success<T>(data: T?):Resources<T>(data = data)
    class Error<T>(throwable: Throwable?):Resources<T>(throwable = throwable)
}