package se.example.mushroommapper.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.detail.DetailsUiState

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel?, // optional?? eller inte?
    placeId: String,
    onNavigate:() -> Unit
){
    val detailsUiState = detailViewModel?.detailsUiState ?: DetailsUiState()

    val isFormsNotBlank = detailsUiState.place.isNotBlank() &&
            detailsUiState.title.isNotBlank()

    val isNotNull = detailsUiState.latitude != null && detailsUiState.longitude != null


        val isPlaceIdNotBlank = placeId.isNotBlank()
        val icon = if(isPlaceIdNotBlank) Icons.Default.Refresh
        else Icons.Default.Check
        LaunchedEffect(key1 = Unit){
            if(isPlaceIdNotBlank){
                detailViewModel?.getPlace(placeId)
            }else{
                detailViewModel?.resetState()
            }
        }
        val scope = rememberCoroutineScope()

        val scaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
            floatingActionButton = {
                AnimatedVisibility(visible = isFormsNotBlank) {
                    FloatingActionButton(
                        onClick = {
                            if(isPlaceIdNotBlank){
                                detailViewModel?.updatePlace(placeId)
                            } else {
                                detailViewModel?.addPlace()
                            }
                        }
                    ) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                }
            },
        ) { padding ->
            Column(modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Gray)
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

                /*LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    contentPadding = PaddingValues(
                        vertical = 16.dp, horizontal = 8.dp
                    )
                ){
                    itemsIndexed(Utils.colors){ colorIndex, color ->
                        ColorItem(color = color) {
                            detailViewModel?.onColorChange(colorIndex)
                        }

                    }
                }*/

                OutlinedTextField(value = detailsUiState.title,
                    onValueChange = {
                        detailViewModel?.onTitleChange(it)
                    },
                    label = { Text(text = "Title")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    value = detailsUiState.place,
                    onValueChange = { detailViewModel?.onPlaceChange(it)},
                    label = { Text(text = "Description")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp)
                )

                OutlinedTextField(
                    value = if (detailsUiState.latitude == null) "" else detailsUiState.latitude.toString(),
                    onValueChange = { detailViewModel?.onLatitudeChange(it)},
                    label = { Text(text = "Latitude")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    value = if (detailsUiState.longitude == null) "" else detailsUiState.longitude.toString(),
                    onValueChange = { detailViewModel?.onLongitudeChange(it)},
                    label = { Text(text = "Longitude")},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

            }
        }
}


@Composable
fun ColorItem(
    color: Color,
    onClick:() -> Unit
){
    Surface(
        color = color,
        shape = CircleShape,
        modifier = Modifier
            .padding(8.dp)
            .size(36.dp)
            .clickable {
                onClick.invoke()
            },
        border = BorderStroke(2.dp, Color.Black)
    ) {

    }

}

object Utils {
    val colors = listOf(
        Color(0xFFffffff),
        Color(0xFFff80ed),
        Color(0xFFff8000),
        Color(0xFFFFD700),
        Color(0xFFFFA500),
        Color(0xFFfa8072),
        Color(0xFF20b2aa),
        Color(0xFF00ff7f),
        Color(0xFFcc0000),
        Color(0xFFff7f50),
    )
}

