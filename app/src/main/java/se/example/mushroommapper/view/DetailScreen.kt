package se.example.mushroommapper.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import se.example.mushroommapper.R
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.detail.DetailsUiState

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel?,
    placeId: String,
    onNavigate: () -> Unit
) {
    val detailsUiState = detailViewModel?.detailsUiState ?: DetailsUiState()
    val isPlaceIdNotBlank = placeId.isNotBlank()
    if (isPlaceIdNotBlank) Icons.Default.Refresh
    else Icons.Default.Check
    LaunchedEffect(key1 = Unit) {
        if (isPlaceIdNotBlank) {
            detailViewModel?.getPlace(placeId)
        } else {
            detailViewModel?.resetState()
        }
    }
    val shouldDisplayError = remember { mutableStateOf(false) }
    val errorMessage = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(padding)
        ) {
            if (detailsUiState.placeAddedStatus) {
                val msg = stringResource(id = R.string.AddedPlaceSuccessfully)
                LaunchedEffect(key1 = null) {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(msg)
                        detailViewModel?.resetPlaceAddedStatus()
                        onNavigate.invoke()
                    }
                }
            }
            if (detailsUiState.updatedPlaceStatus) {
                val msg = stringResource(id = R.string.PlaceUpdatedSuccessfully)
                LaunchedEffect(key1 = null) {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(msg)
                        detailViewModel?.resetPlaceAddedStatus()
                        onNavigate.invoke()
                    }
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

                    val titleInput = detailsUiState.title.trim()
                    val placeInput = detailsUiState.place.trim()
                    val latitudeInput = detailsUiState.latitude?.toString()?.trim()
                    val longitudeInput = detailsUiState.longitude?.toString()?.trim()

                    if (titleInput.isEmpty()) {
                        errorMessage.value = "Title is required"
                        shouldDisplayError.value = true
                        return@Button
                    }

                    if (placeInput.isEmpty()) {
                        errorMessage.value = "Description is required"
                        shouldDisplayError.value = true
                        return@Button
                    }

                    val latitude = latitudeInput?.toDoubleOrNull()
                    val longitude = longitudeInput?.toDoubleOrNull()

                    if (latitudeInput.isNullOrEmpty()) {
                        errorMessage.value = "Latitude is required"
                        shouldDisplayError.value = true
                        return@Button
                    }

                    if (latitude == null || latitude < -90 || latitude > 90) {
                        errorMessage.value = "Latitude is invalid or out of range (-90 to 90)"
                        shouldDisplayError.value = true
                        return@Button
                    }

                    if (longitudeInput.isNullOrEmpty()) {
                        errorMessage.value = "Longitude is required"
                        shouldDisplayError.value = true
                        return@Button
                    }

                    if (longitude == null || longitude < -180 || longitude > 180) {
                        errorMessage.value = "Longitude is invalid or out of range (-180 to 180)"
                        shouldDisplayError.value = true
                        return@Button
                    }

                    if (!shouldDisplayError.value) {
                        if (isPlaceIdNotBlank) {
                            detailViewModel?.updatePlace(placeId)
                        } else {
                            detailViewModel?.addPlace()
                        }
                    }
                }) {
                    Text(text = stringResource(id = R.string.UpdatePlace))
                }
                if (shouldDisplayError.value) {
                    ErrorDialog(errorMessage.value) {
                        shouldDisplayError.value = false
                    }
                }
            }
        }
    }
}


