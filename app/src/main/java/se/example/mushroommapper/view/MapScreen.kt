package se.example.mushroommapper.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import se.example.mushroommapper.model.Place


@Composable
fun MapScreen() {
    val singapore = LatLng(1.35, 103.87)

    val places = listOf(
        Place("Singapore","Nice city",1.36,103.87),
        Place("Singapore2","Nice city",1.39,103.87),
        Place("Singapore3","Nice city",1.41,103.87)
    )

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