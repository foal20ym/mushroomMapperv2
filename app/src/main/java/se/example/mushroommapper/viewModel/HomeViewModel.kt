package se.example.mushroommapper.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import se.example.mushroommapper.data.AuthRepositoryImpl
import se.example.mushroommapper.data.Resources
import se.example.mushroommapper.data.StorageRepository
import se.example.mushroommapper.model.Notes

class HomeViewModel(
    private val repository: StorageRepository = StorageRepository(),
    //private val authRepository: AuthRepositoryImpl
): ViewModel() {
    var homeUIState by mutableStateOf(HomeUIState())

    val user = repository.user()
    val hasUser: Boolean
        get() = repository.hasUser()
    private val userId: String
        get() = repository.getUserId()

    fun loadNotes(){
        if(hasUser){
            if(userId.isNotBlank()){
                getUserNotes(userId)
            }
        } else {
            homeUIState = homeUIState.copy(notesList = Resources.Error(
                throwable = Throwable(message = "User is not Logged in")
            ))
        }
    }

    private fun getUserNotes(userId: String) = viewModelScope.launch {
        repository.getUserNotes(userId).collect {
            homeUIState = homeUIState.copy(notesList = it)
        }
    }

    fun deleteNote(noteId: String) = repository.deleteNote(noteId){
        homeUIState = homeUIState.copy(noteDeletedStatus = it)
    }

    //fun signOut() = authRepository.signOut()


}

data class HomeUIState(
    val notesList: Resources<List<Notes>> = Resources.Loading(),
    val noteDeletedStatus: Boolean = false
)