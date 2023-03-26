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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import se.example.mushroommapper.LocationViewModel
import se.example.mushroommapper.R
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
    detailViewModel: DetailViewModel?,
    onNavigate:() -> Unit
) {

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(padding)
        ) {
            if (detailsUiState.placeAddedStatus) {
                val msg = stringResource(id = R.string.AddedPlaceSuccessfully)
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar(msg)
                    detailViewModel?.resetPlaceAddedStatus()
                    onNavigate.invoke()
                }
            }
            if (detailsUiState.updatedPlaceStatus) {
                val msg = stringResource(id = R.string.PlaceUpdatedSuccessfully)
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar(msg)
                    detailViewModel?.resetPlaceAddedStatus()
                    onNavigate.invoke()
                }
            }

            TextField(
                value = detailsUiState.title,
                onValueChange = {
                    detailViewModel?.onTitleChange(it)
                },
                label = { Text(text = stringResource(id = R.string.Title)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            TextField(
                value = detailsUiState.place,
                onValueChange = { detailViewModel?.onPlaceChange(it) },
                label = { Text(text = stringResource(id = R.string.Description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            TextField(
                value = if (detailsUiState.latitude == null) "" else detailsUiState.latitude.toString(),
                onValueChange = { detailViewModel?.onLatitudeChange(it) },
                label = { Text(text = "Latitude") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )
            TextField(
                value = if (detailsUiState.longitude == null) "" else detailsUiState.longitude.toString(),
                onValueChange = { detailViewModel?.onLongitudeChange(it) },
                label = { Text(text = "Longitude") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = {
                    var lat: Double? = location?.latitude?.toDouble()
                    var lng: Double? = location?.longitude?.toDouble()


                    detailViewModel?.onLatitudeChange(lat.toString())
                    detailViewModel?.onLongitudeChange(lng.toString())

                }) {
                    Text(text = stringResource(id = R.string.GetCoordinates))
                }
            }
        }
    }
}



