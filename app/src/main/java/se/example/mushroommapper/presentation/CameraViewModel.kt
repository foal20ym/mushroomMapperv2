package se.example.mushroommapper.presentation

import android.content.Context
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
            repo.captureAndSaveImage(context)
        }
    }
}
