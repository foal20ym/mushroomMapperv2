package se.example.mushroommapper.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import se.example.mushroommapper.data.AuthRepository
import se.example.mushroommapper.data.Resource
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    val _resetPasswordSate = Channel<ResetPasswordState>()
    val resetPasswordState = _resetPasswordSate.receiveAsFlow()

    fun resetPassword(email: String) = viewModelScope.launch{
        repository.resetPassword(email).collect{ result ->
            when(result){
                is Resource.Success -> {
                    _resetPasswordSate.send(ResetPasswordState(isSuccess = "Password Reset Successfully"))
                }
                is Resource.Loading -> {
                    _resetPasswordSate.send(ResetPasswordState(isLoading = true))
                }
                is Resource.Error -> {
                    _resetPasswordSate.send(ResetPasswordState(isError = result.message))
                }
            }
        }
    }
}