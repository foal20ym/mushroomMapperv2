package se.example.mushroommapper.view

import android.os.Build
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import se.example.mushroommapper.R
import se.example.mushroommapper.navigation.Graph
import se.example.mushroommapper.viewModel.CameraViewModel
import java.util.*



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(viewModel: CameraViewModel = hiltViewModel(), navController: NavController) {
    val permissions = if (Build.VERSION.SDK_INT <= 28) {
        listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else listOf(android.Manifest.permission.CAMERA)

    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    if (!permissionState.allPermissionsGranted) {
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }

    var previewView: PreviewView
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        if (permissionState.allPermissionsGranted) {
            AndroidView(
                factory = {
                    previewView = PreviewView(it)
                    viewModel.showCameraPreview(previewView, lifecycleOwner)
                    previewView
                }, modifier = Modifier.fillMaxSize())
            IconButton(onClick = {
                viewModel.captureAndSave(context)
                navController.navigate(Graph.PHOTO)

            },
                content = {
                    Icon(
                        imageVector = Icons.Sharp.Lens,
                        contentDescription = stringResource(id = R.string.TakePhoto),
                        tint = Color.White,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(1.dp)
                            .border(1.dp, Color.White, CircleShape)
                    )
                }

            )
        }
    }
}