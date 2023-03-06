package se.example.mushroommapper.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import se.example.mushroommapper.data.Resources
import se.example.mushroommapper.data.StorageRepository
import se.example.mushroommapper.model.Places

class MapViewModel(
    private val repository: StorageRepository = StorageRepository(),
    //private val authRepository: AuthRepositoryImpl
): ViewModel() {
    var mapUIState by mutableStateOf(MapUIState())

    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()
    private val userId: String
        get() = repository.getUserId()

    fun loadPlaces(){
        if(hasUser){
            if(userId.isNotBlank()){
                getUserPlaces(userId)
            }
        } else {
            mapUIState = mapUIState.copy(placeList = Resources.Error(
                throwable = Throwable(message = "User is not Logged in")
            ))
        }
    }

    private fun getUserPlaces(userId: String) = viewModelScope.launch {
        repository.getUserPlaces(userId).collect {
            mapUIState = mapUIState.copy(placeList = it)
        }
    }

    fun deleteNote(noteId: String) = repository.deleteNote(noteId){
        mapUIState = mapUIState.copy(placeDeletedStatus = it)
    }
}

data class MapUIState(
    val placeList: Resources<List<Places>> = Resources.Loading(),
    val placeDeletedStatus: Boolean = false
)