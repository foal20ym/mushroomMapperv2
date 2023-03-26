package se.example.mushroommapper.view

import android.media.Image
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import se.example.mushroommapper.viewModel.CameraViewModel
import coil.compose.rememberImagePainter
import se.example.mushroommapper.R


@Composable
fun PhotoScreen(viewModel: CameraViewModel = hiltViewModel(), navController: NavController) {
    val photoUri = viewModel.photoUri
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        if (photoUri != null) {
            Image(
               painter = rememberAsyncImagePainter(photoUri),
               contentDescription = stringResource(id = R.string.CapturedPhoto)
            )
        } else {
            Text(stringResource(id = R.string.NoImageCaptured))
        }
    }
}