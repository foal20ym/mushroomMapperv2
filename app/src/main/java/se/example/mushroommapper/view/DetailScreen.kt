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
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import se.example.mushroommapper.BottomBarScreen
import se.example.mushroommapper.navigation.HomeNavGraph
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.detail.DetailsUiState
import se.example.mushroommapper.navigation.DetailsScreen
import se.example.mushroommapper.viewModel.HomeUIState
import se.example.mushroommapper.viewModel.HomeViewModel

@Composable
fun DetailScreen(
    detailViewModel: DetailViewModel?, // optional?? eller inte?
    noteId: String,
    onNavigate:() -> Unit
){
    val detailsUiState = detailViewModel?.detailsUiState ?: DetailsUiState()

    val isFormsNotBlank = detailsUiState.note.isNotBlank() &&
            detailsUiState.title.isNotBlank()

    val isNoteIdNotBlank = noteId.isNotBlank()
    val icon = if(isNoteIdNotBlank) Icons.Default.Refresh
        else Icons.Default.Check
    LaunchedEffect(key1 = Unit){
        if(isNoteIdNotBlank){
            detailViewModel?.getNote(noteId)
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
                        if(isNoteIdNotBlank){
                            detailViewModel?.updateNote(noteId)
                        } else {
                            detailViewModel?.addNote()
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
            if(detailsUiState.noteAddedStatus){
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("Added Place Successfully")
                    detailViewModel?.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }
            if(detailsUiState.updatedNoteStatus){
                scope.launch {
                    scaffoldState.snackbarHostState
                        .showSnackbar("Place updated successfully")
                    detailViewModel?.resetNoteAddedStatus()
                    onNavigate.invoke()
                }
            }

            LazyRow(
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
            }
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
                value = detailsUiState.note,
                onValueChange = { detailViewModel?.onNoteChange(it)},
                label = { Text(text = "Notes")},
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
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

