package se.example.mushroommapper.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import se.example.mushroommapper.LocationViewModel
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.detail.DetailsUiState
import se.example.mushroommapper.model.LocationDetails

private @Composable
fun GPS(location: LocationDetails?) {
    location?.let {
        Text(text = location.latitude)
        Text(text = location.longitude)
    }
}

@Composable
fun ManuallyAddLocationScreen(
    detailViewModel: DetailViewModel?, // optional?? eller inte?
    onNavigate:() -> Unit
){

    val locationViewModel = viewModel(modelClass = LocationViewModel::class.java)
    val location by locationViewModel.getLocationLiveData().observeAsState()


    val detailsUiState = detailViewModel?.detailsUiState ?: DetailsUiState()

    val isFormsNotBlank = detailsUiState.place.isNotBlank() &&
            detailsUiState.title.isNotBlank()

    val isNotNull = detailsUiState.latitude != null && detailsUiState.longitude != null

    val context = LocalContext.current
    val localContext = LocalContext.current

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            AnimatedVisibility(visible = isFormsNotBlank) {
                FloatingActionButton(
                    onClick = {
                        detailViewModel?.addPlace()
                    }
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                }
            }
        },
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .padding(padding)
        ) {
            if(detailsUiState.placeAddedStatus){
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("Added Place Successfully")
                    detailViewModel?.resetPlaceAddedStatus()
                    onNavigate.invoke()
                }
            }
            if(detailsUiState.updatedPlaceStatus){
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("Place updated successfully")
                    detailViewModel?.resetPlaceAddedStatus()
                    onNavigate.invoke()
                }
            }

           /* GPS(location)

            Text(text="HELLO GPS HERE")
            Text(text="Latitude: ${location?.latitude}")
            Text(text="Longitude: ${location?.longitude}")

            val lat = location?.latitude?.toDouble()
            val lng = location?.longitude?.toDouble()

            Text(text="lat: ${lat}, lng: $lng") */

            TextField(value = detailsUiState.title,
                onValueChange = {
                    detailViewModel?.onTitleChange(it)
                },
                label = { Text(text = "Title")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            TextField(
                value = detailsUiState.place,
                onValueChange = { detailViewModel?.onPlaceChange(it)},
                label = { Text(text = "Description")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            TextField(
                value = if (detailsUiState.latitude == null) "" else detailsUiState.latitude.toString(),
                onValueChange = { detailViewModel?.onLatitudeChange(it)},
                label = { Text(text = "Latitude")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            TextField(
                value = if (detailsUiState.longitude == null) "" else detailsUiState.longitude.toString(),
                onValueChange = { detailViewModel?.onLongitudeChange(it)},
                label = { Text(text = "Longitude")},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    val lat0: Double? = location?.latitude?.toDouble()
                    val lng0: Double? = location?.longitude?.toDouble()
                    val lat1: Double = String.format("%.3f", lat0).toDouble()
                    val lat2: Double = String.format("%.2f", lat1).toDouble()
                    val lng1: Double = String.format("%.3f", lng0).toDouble()
                    val lng2: Double = String.format("%.2f", lng1).toDouble()

                    detailViewModel?.onLatitudeChange(lat2.toString())
                    detailViewModel?.onLongitudeChange(lng2.toString())

                }) {
                    Text(text = "Get the coordinates by clicking here")
                }
            }
        }
    }
}


