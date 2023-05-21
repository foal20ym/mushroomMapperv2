package se.example.mushroommapper.data

import android.content.Context
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner

interface CustomCameraRepo {
    suspend fun captureAndSaveImage(
        context: Context,
        onImageSavedCallback: (Uri) -> Unit,
        onErrorCallback: (Exception) -> Unit
    )
    suspend fun showCameraPreview(previewView: PreviewView, lifecycleOwner: LifecycleOwner)

}