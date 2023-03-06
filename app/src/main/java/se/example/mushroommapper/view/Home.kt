
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import se.example.mushroommapper.data.Resources
import se.example.mushroommapper.model.Notes
import se.example.mushroommapper.view.Utils
import se.example.mushroommapper.viewModel.HomeUIState
import se.example.mushroommapper.viewModel.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Home(
    homeViewModel: HomeViewModel?,
    onNoteClick:(id:String) -> Unit,
    navToDetailPage:() -> Unit, // Ta bort sen
    navToLoginPage:() -> Unit, // Ta bort sen
){
    val homeUIState = homeViewModel?.homeUIState ?: HomeUIState()

    var openDialog by remember {
        mutableStateOf(false)
    }

    var selectedNote: Notes? by remember {
        mutableStateOf(null)
    }

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()

    Scaffold(

    ) { padding -> 
        Column(modifier = Modifier.padding(padding)) {
            when(homeUIState.notesList){
                is Resources.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
                is Resources.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                    ){
                        items(
                            homeUIState.notesList.data ?: emptyList()
                        ) { note ->
                            NoteItem(notes = note,
                                onLongClick = {
                                    openDialog = true
                                    selectedNote = note
                                },
                            ) {
                                onNoteClick.invoke(note.documentId)
                            }
                        }
                        
                    }
                    AnimatedVisibility(visible = openDialog) {
                        AlertDialog(onDismissRequest = {
                            openDialog = false
                        },
                           title = { Text(text = "Delete Note?")},
                           confirmButton = {
                               Button(
                                   onClick = {
                                       selectedNote?.documentId?.let {
                                           homeViewModel?.deleteNote(it)
                                       }
                                       openDialog = false
                                   },
                                   colors = ButtonDefaults.buttonColors(
                                       backgroundColor = Color.Red
                                   ),
                               ) {
                                   Text(text = "Delete")
                               }
                           },
                            dismissButton = {
                                Button(onClick = { openDialog = false }) {
                                    Text(text = "Cancel")
                                }
                            }
                        )



                    }
                }
                else -> {
                    Text(
                        text = homeUIState
                            .notesList.throwable?.localizedMessage ?: "Unknown Error",
                        color = Color.Red
                    )
                }

            } // when
        } // column

    }
    LaunchedEffect(key1 = homeViewModel?.hasUser){
        if(homeViewModel?.hasUser == false){
            navToLoginPage.invoke()
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    notes: Notes,
    onLongClick:() -> Unit,
    onClick:() -> Unit,
){
    Card(
        modifier = Modifier
            .combinedClickable(
                onLongClick = { onLongClick.invoke() },
                onClick = { onClick.invoke() }
            )
            .padding(8.dp)
            .fillMaxWidth(),
        backgroundColor = Utils.colors[notes.colorIndex]
    ){
        Column {
            Text(
                text = notes.title,
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Clip,
                modifier = Modifier.padding(4.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.disabled
            ) {
                Text(
                    text = notes.description,
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp),
                    maxLines = 4
                )
            }
            Spacer(modifier = Modifier.size(4.dp))
            CompositionLocalProvider(
                LocalContentAlpha provides ContentAlpha.disabled
            ) {
                Text(
                    text = formatDate(notes.timestamp),
                    style = MaterialTheme.typography.body1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp)
                        .align(Alignment.End),
                    maxLines = 4
                )
            }
        }


    }


}

private fun formatDate(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("MM-dd-yy hh:mm", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}

