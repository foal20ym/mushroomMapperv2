package se.example.mushroommapper.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import se.example.mushroommapper.data.StorageRepository
import se.example.mushroommapper.model.Notes
import se.example.mushroommapper.model.Places

class DetailViewModel(
    private val repository: StorageRepository = StorageRepository()
): ViewModel() {
    var detailsUiState by mutableStateOf(DetailsUiState())
        private set

    private val hasUser: Boolean
        get() = repository.hasUser()
    private val user:FirebaseUser?
        get() = repository.user()

    fun onColorChange(colorIndex: Int){
        detailsUiState = detailsUiState.copy(colorIndex = colorIndex)
    }
    fun onTitleChange(title:String) {
        detailsUiState = detailsUiState.copy(title = title)
    }
    fun onNoteChange(note:String) {
        detailsUiState = detailsUiState.copy(note = note)
    }

    fun addNote() {
        if(hasUser){
            repository.addNote(
                userId = user!!.uid,
                title = detailsUiState.title,
                description = detailsUiState.note,
                color = detailsUiState.colorIndex,
                timestamp = Timestamp.now()
            ) {
                detailsUiState = detailsUiState.copy(noteAddedStatus = it)
            }
        }
    }

    fun setEditFields(note: Notes){
        detailsUiState = detailsUiState.copy(
            colorIndex = note.colorIndex,
            title = note.title,
            note = note.description
        )
    }

    fun setEditFieldsPlace(place: Places){
        detailsUiState = detailsUiState.copy(
            title = place.title,
            note = place.description
        )
    }

    fun getNote(noteId: String){
        repository.getNote(
            noteId = noteId,
            onError = {}
        ) {
            detailsUiState = detailsUiState.copy(selectedNote = it)
            detailsUiState.selectedNote?.let { it1 -> setEditFields(it1) }
        }
    }


    fun updateNote(
        noteId: String
    ){
        repository.updateNote(
            title = detailsUiState.title,
            note = detailsUiState.note,
            noteId = noteId,
            color = detailsUiState.colorIndex
        ){
            detailsUiState = detailsUiState.copy(updatedNoteStatus = it)
        }
    }

    fun resetNoteAddedStatus(){
        detailsUiState = detailsUiState.copy(
            noteAddedStatus = false,
            updatedNoteStatus = false
        )
    }

    fun resetState(){
        detailsUiState = DetailsUiState()
    }

    /*
       fun addPlace() {
        if(hasUser){
            repository.addPlace(
                userId = user!!.uid,
                title = detailsUiState.title,
                description = detailsUiState.note,
                timestamp = Timestamp.now()
            ) {
                detailsUiState = detailsUiState.copy(placeAddedStatus = it)
            }
        }
    }
    fun getPlace(placeId: String){
        repository.getPlace(
            placeId = placeId,
            onError = {}
        ) {
            detailsUiState = detailsUiState.copy(selectedPlace = it)
            detailsUiState.selectedPlace?.let { it1 -> setEditFieldsPlace(it1) }
        }
    }   fun addPlace() {
        if(hasUser){
            repository.addPlace(
                userId = user!!.uid,
                title = detailsUiState.title,
                description = detailsUiState.note,
                timestamp = Timestamp.now()
            ) {
                detailsUiState = detailsUiState.copy(placeAddedStatus = it)
            }
        }
    }
    fun getPlace(placeId: String){
        repository.getPlace(
            placeId = placeId,
            onError = {}
        ) {
            detailsUiState = detailsUiState.copy(selectedPlace = it)
            detailsUiState.selectedPlace?.let { it1 -> setEditFieldsPlace(it1) }
        }
    }
     */

}
data class DetailsUiState(
    val colorIndex: Int = 0,
    val title: String = "",
    val note: String = "",
    val noteAddedStatus: Boolean = false,
    val updatedNoteStatus: Boolean = false,
    val selectedNote: Notes? = null,
    val placeAddedStatus: Boolean = false,
    val updatedPlaceStatus: Boolean = false,
    val selectedPlace: Places? = null
)