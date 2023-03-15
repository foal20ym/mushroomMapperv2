package se.example.mushroommapper.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import se.example.mushroommapper.domain.repository.CustomCameraRepo
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    private val repo: CustomCameraRepo
): ViewModel() {
   // private lateinit var photoUri: Uri

    private var _photoUri: Uri? = null
    val photoUri: Uri?
        get() = _photoUri
    /*fun getPhotoUri() {
        viewModelScope.launch {
            photoUri = repo.getPhotoUri()
            Log.e("image", "$photoUri")
        }
    }*/
    fun showCameraPreview(
        previewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) {
        viewModelScope.launch {
            repo.showCameraPreview(previewView, lifecycleOwner)
        }
    }

    fun captureAndSave(context: Context) {
        viewModelScope.launch {
            repo.captureAndSaveImage(context, {uri ->
                _photoUri = uri
            }, {exception ->
                //handle error
            })
        }
    }
}
