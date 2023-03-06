package se.example.mushroommapper.data

import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import se.example.mushroommapper.model.Notes
import se.example.mushroommapper.model.Places


const val NOTES_COLLECTION_REF = "notes"
const val PLACES_COLLECTION_REF = "places"

class StorageRepository {

    fun user() = Firebase.auth.currentUser
    fun hasUser(): Boolean = Firebase.auth.currentUser != null
    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val notesRef: CollectionReference = Firebase.firestore.collection(NOTES_COLLECTION_REF)
    private val placeRef: CollectionReference = Firebase.firestore.collection(PLACES_COLLECTION_REF)

    fun getUserNotes(
        userId: String
    ):Flow<Resources<List<Notes>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = notesRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null){
                        val notes = snapshot.toObjects(Notes::class.java)
                        Resources.Success(data = notes)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend( response )

                }

        }catch (e:Exception){
            trySend( Resources.Error(e?.cause))
            e.printStackTrace()
        }
        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    fun getNote(
        noteId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Notes?) -> Unit
    ){
        notesRef
            .document(noteId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Notes::class.java))
            }
            .addOnFailureListener { result ->
                onError.invoke(result.cause)
            }
    }

    fun addNote(
        userId: String,
        title: String,
        description: String,
        timestamp: Timestamp,
        color: Int = 0,
        onComplete: (Boolean) -> Unit,
    ){
        val documentId = notesRef.document().id
        val note = Notes(userId, title, description, timestamp, colorIndex = color, documentId = documentId)

        notesRef
            .document(documentId)
            .set(note)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    fun deleteNote(noteId: String, onComplete: (Boolean) -> Unit){
        notesRef.document(noteId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    fun updateNote(
        title: String,
        note: String,
        color: Int,
        noteId: String,
        onResult: (Boolean) -> Unit
    ){
        val updateData = hashMapOf<String, Any>(
            "colorIndex" to color,
            "description" to note,
            "title" to title
        )

        notesRef.document(noteId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }
    }


    fun getUserPlaces(
        userId: String
    ):Flow<Resources<List<Places>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = placeRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null){
                        val places = snapshot.toObjects(Places::class.java)
                        Resources.Success(data = places)
                    } else {
                        Resources.Error(throwable = e?.cause)
                    }
                    trySend( response )

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
        notesRef
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
        timestamp: Timestamp,
        onComplete: (Boolean) -> Unit,
    ){
        val documentId = placeRef.document().id
        val place = Places(userId, title, description, latitude = 0.0, longitude = 0.0, timestamp, documentId = documentId)

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