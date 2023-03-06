package se.example.mushroommapper.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import se.example.mushroommapper.model.Places
import se.example.mushroommapper.viewModel.DataViewModel
import se.example.mushroommapper.viewModel.MapViewModel


@Composable
fun MapScreen(
    dataViewModel: DataViewModel = viewModel(),
) {
    val singapore = LatLng(1.35, 103.87)

    val places = listOf(
        Places("Singapore","Nice city", "cool city",1.36,103.87),
        Places("Singapore2","Nice city", "cool city",1.39,103.87),
        Places("Singapore3","Nice city", "cool city",1.42,103.87),
    )

    val getData = dataViewModel.state.value
    val locationn = LatLng(getData.latitude, getData.longitude)
    val places2 = getData


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