package se.example.mushroommapper.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import se.example.mushroommapper.LocationViewModel
import se.example.mushroommapper.model.Places
import se.example.mushroommapper.viewModel.HomeViewModel


@Composable
fun MapScreen(
    homeViewModel: HomeViewModel,
) {

    val homeUIState = homeViewModel.homeUIState
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit){
        homeViewModel.loadPlaces()
    }

    val places = mutableListOf<Places>()

    for(place in homeUIState.placesList.data ?: emptyList()){
        places.add(Places(place.userId,place.title, place.description,place.latitude,place.longitude))
    }
    val locationViewModel = viewModel(modelClass = LocationViewModel::class.java)
    val location by locationViewModel.getLocationLiveData().observeAsState()
    val singapore = LatLng(1.35, 103.87)
    val lat = location?.longitude?.toDoubleOrNull()
    val lng = location?.longitude?.toDoubleOrNull()

    if (lat != null && lng != null) {
        val latestLocation = LatLng(lat, lng)
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(latestLocation, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            for(place in places) {
                Marker(
                    state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                    title = place.title,
                    snippet = place.description,
                )
            }
        }
    } else {
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(singapore, 10f)
        }
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            for(place in places) {
                Marker(
                    state = MarkerState(position = LatLng(place.latitude, place.longitude)),
                    title = place.title,
                    snippet = place.description,
                )
            }
        }
    }
}