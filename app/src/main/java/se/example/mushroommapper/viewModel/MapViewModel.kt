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
    //private val authRepository: AuthRepositoryImpl // Lägg till? testa om det funkar med ev
): ViewModel() {
    var homeUIState by mutableStateOf(HomeUIState())

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
            homeUIState = homeUIState.copy(placesList = Resources.Error(
                throwable = Throwable(message = "User is not Logged in")
            ))
        }
    }

    private fun getUserPlaces(userId: String) = viewModelScope.launch {
        repository.getUserPlaces(userId).collect {
            homeUIState = homeUIState.copy(placesList = it)
        }
    }

    fun deletePlace(placeId: String) = repository.deletePlace(placeId){
        homeUIState = homeUIState.copy(placeDeletedStatus = it)
    }

    //fun signOut() = authRepository.signOut()


}

data class MapUIState(
    val placesList: Resources<List<Places>> = Resources.Loading(),
    val placeDeletedStatus: Boolean = false
)