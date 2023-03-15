package se.example.mushroommapper.data.repository

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.core.ImageCapture.OnImageSavedCallback
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import se.example.mushroommapper.domain.repository.CustomCameraRepo
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class CustomCameraImpl @Inject constructor(
    private val cameraProvider: ProcessCameraProvider,
    private val selector: CameraSelector,
    private val preview: Preview,
    private val imageAnalysis: ImageAnalysis,
    private val imageCapture: ImageCapture
    ): CustomCameraRepo {
    override suspend fun captureAndSaveImage(
        context: Context,
        onImageSavedCallback: (Uri) -> Unit,
        onErrorCallback: (Exception) -> Unit
    ) {
        val name = SimpleDateFormat(
            "yyyy-MM-dd-HH-mm-ss-SSS",
            Locale.ROOT
        ).format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT > 28) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/My-Camera-App-Images")
            }
        }
        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()


        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object: ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // uri for the picture
                    onImageSavedCallback(outputFileResults.savedUri!!)
                    Toast.makeText(
                        context,
                        "Saved image ${outputFileResults.savedUri!!}",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    onErrorCallback.invoke(exception)
                    Toast.makeText(
                        context,
                        "Error: ${exception.message!!}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )
    }


    override suspend fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        preview.setSurfaceProvider(previewView.surfaceProvider)
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, imageAnalysis, imageCapture)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}