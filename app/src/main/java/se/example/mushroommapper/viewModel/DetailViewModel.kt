package se.example.mushroommapper.detail

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import se.example.mushroommapper.data.StorageRepository
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

    fun onPlaceChange(place:String) {
        detailsUiState = detailsUiState.copy(place = place)
    }
    fun onLatitudeChange(latitude: String) {
        try {
            detailsUiState = detailsUiState.copy(latitude = latitude.toDouble())
        } catch(e:Exception) {
            println(e)
        }
    }
    fun onLongitudeChange(longitude: String) {
        try {
            detailsUiState = detailsUiState.copy(longitude = longitude.toDouble())
        } catch(e:Exception) {
            println(e)
        }
    }

    fun addImage(
        path: Uri
    ){
        if(hasUser){
            repository.addImage(
                path = path,
                timeStamp = Timestamp.now(),
                userId = user!!.uid
            ) {
                detailsUiState = detailsUiState.copy(placeAddedStatus = it)
            }
        }
    }

    fun addPlace() {

        if(hasUser){
            repository.addPlace(
                userId = user!!.uid,
                title = detailsUiState.title,
                description = detailsUiState.place,
                latitude = detailsUiState.latitude!!,
                longitude = detailsUiState.longitude!!,
                timestamp = Timestamp.now()
            ) {
                detailsUiState = detailsUiState.copy(placeAddedStatus = it)
            }
        }
    }

    fun setEditFieldsPlace(place: Places){
        detailsUiState = detailsUiState.copy(
            title = place.title,
            place = place.description
        )
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

    fun updatePlace(
        placeId: String
    ){
        repository.updatePlace(
            title = detailsUiState.title,
            description = detailsUiState.place,
            placeId = placeId,
        ){
            detailsUiState = detailsUiState.copy(updatedPlaceStatus = it)
        }
    }

    fun resetPlaceAddedStatus(){
        detailsUiState = detailsUiState.copy(
            placeAddedStatus = false,
            updatedPlaceStatus = false
        )
    }

    fun resetState(){
        detailsUiState = DetailsUiState()
    }


}

data class DetailsUiState(
    val colorIndex: Int = 0,
    val title: String = "",
    val place: String = "",
    val latitude: Double ?= null,
    val longitude: Double ?= null,
    val placeAddedStatus: Boolean = false,
    val updatedPlaceStatus: Boolean = false,
    val selectedPlace: Places? = null
)