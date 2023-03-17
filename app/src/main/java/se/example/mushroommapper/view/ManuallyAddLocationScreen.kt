package se.example.mushroommapper.view

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import se.example.mushroommapper.detail.DetailViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import se.example.mushroommapper.ApplicationViewModel
import se.example.mushroommapper.LocationDetails
import se.example.mushroommapper.detail.DetailsUiState
import java.util.concurrent.Executors

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

    val applicationViewModel = viewModel(modelClass = ApplicationViewModel::class.java)
    val location by applicationViewModel.getLocationLiveData().observeAsState()


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


            GPS(location)

            Text(text="HELLO GPS HERE")
            Text(text="${location}")

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
            Button(onClick = { /*TODO*/ 
                
            }) {
                Text(text = "Get location by clicking here")
            }

        }
    }
}


