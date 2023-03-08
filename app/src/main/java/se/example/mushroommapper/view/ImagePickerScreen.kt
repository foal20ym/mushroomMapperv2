package se.example.mushroommapper.view

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.ExifInterface
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import java.io.File
import java.net.URI

/*
class ImageViewModel(

) : ViewModel(){
    var imageUri = { mutableStateOf<Uri?>(null) }

    val bitmap =  { mutableStateOf<Bitmap?>(null)}

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uri: Uri? ->
        imageUri = uri
    }
    val picker =

    imageUri?.let{ uri ->
            val d = File(uri.path)
            val x:ExifInterface = ExifInterface(d)
            x.getAttribute("TAG_DATETIME")
            print(x)
            Text(text = "${x}")
        }
}*/

@Composable
fun ImagePicker() {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uri2: Uri?
    val context = LocalContext.current
    val bitmap = remember { mutableStateOf<Bitmap?>(null)}

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uri: Uri? ->
        imageUri = uri
        uri2 = uri
    }
    Column() {
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text(text= "Pick Image")
        }
        Spacer(modifier = Modifier.height(12.dp))

        imageUri?.let{
            if(Build.VERSION.SDK_INT < 28){
                bitmap.value = MediaStore.Images
                    .Media.getBitmap(context.contentResolver,it)
            } else {
                val source = ImageDecoder
                    .createSource(context.contentResolver,it)
                bitmap.value = ImageDecoder.decodeBitmap(source)
            }
            bitmap.value?.let { btm ->
                Image(bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(400.dp))
            }

        }
    }

}