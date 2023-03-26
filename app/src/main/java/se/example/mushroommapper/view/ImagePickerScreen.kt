package se.example.mushroommapper.view

import android.Manifest
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import se.example.mushroommapper.R
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.ui.theme.NON_INTERACTABLE_COLOR
import se.example.mushroommapper.viewModel.color


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImagePicker(
    detailViewModel: DetailViewModel?,
) {
    var LOWEST_PERMISSION_SDK_VALUE = 33
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uri2: Uri?
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null)}

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uri: Uri? ->
        imageUri = uri
        uri2 = uri
    }


    val permissions = if (Build.VERSION.SDK_INT < LOWEST_PERMISSION_SDK_VALUE) {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    } else listOf(Manifest.permission.READ_MEDIA_IMAGES)

    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    if (!permissionState.allPermissionsGranted) {
        SideEffect {
            permissionState.launchMultiplePermissionRequest()
        }
    }
    Column() {
        if (permissionState.allPermissionsGranted) {
            Button(onClick = {
                launcher.launch("image/*")
            }) {
                Text(text = stringResource(id = R.string.PickImage))
            }
            Spacer(modifier = Modifier.height(12.dp))

            imageUri?.let {
                if (Build.VERSION.SDK_INT < 28) {
                    bitmap.value = MediaStore.Images
                        .Media.getBitmap(context.contentResolver, it)
                } else {
                    val source = ImageDecoder
                        .createSource(context.contentResolver, it)
                    bitmap.value = ImageDecoder.decodeBitmap(source)
                }
                bitmap.value?.let { btm ->
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(400.dp)
                    )
                }
            }

            Button(
                onClick = {
                    detailViewModel?.addImage(imageUri!!)
                }
            ) {
                Text(text = stringResource(id = R.string.Upload))
            }
        } else {
            if(Build.VERSION.SDK_INT < LOWEST_PERMISSION_SDK_VALUE) {
                Text(
                    text = stringResource(id = R.string.PleaseGiveAccessToFiles),
                    style = TextStyle(
                        color = NON_INTERACTABLE_COLOR.color,
                        fontSize = 40.sp,
                    )
                )
            } else {
                Text(
                    text = stringResource(id = R.string.PleaseGiveAccessToImages),
                    style = TextStyle(
                        color = NON_INTERACTABLE_COLOR.color,
                        fontSize =  40.sp
                    )
                )
            }
        }

    }

}