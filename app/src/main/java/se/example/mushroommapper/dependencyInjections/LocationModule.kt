package se.example.mushroommapper.dependencyInjections
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import se.example.mushroommapper.LocationViewModel

val appModule = module {
    viewModel { LocationViewModel(androidApplication()) }
}