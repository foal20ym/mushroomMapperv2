package se.example.mushroommapper.model

import com.google.firebase.Timestamp
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import se.example.mushroommapper.ApplicationViewModel

data class Notes(
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val colorIndex: Int = 0,
    val documentId: String = ""
)

val appModule = module {
    viewModel { ApplicationViewModel(androidApplication()) }
}

