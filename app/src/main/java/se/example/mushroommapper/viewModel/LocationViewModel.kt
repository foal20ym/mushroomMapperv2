package se.example.mushroommapper.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import se.example.mushroommapper.data.LocationLiveData


class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationLiveData = LocationLiveData(application)
    fun getLocationLiveData() = locationLiveData
    fun startLocationUpdates() {
        locationLiveData.startLocationUpdates()
    }
}