package se.example.mushroommapper.viewModel

data class ResetPasswordState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)
