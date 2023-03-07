package se.example.mushroommapper

import android.app.Application
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import se.example.mushroommapper.ui.theme.MushroomMapperTheme



import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import dagger.hilt.android.HiltAndroidApp
import se.example.mushroommapper.navigation.RootNavigationGraph
import se.example.mushroommapper.view.CameraScreen
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.jar.Manifest

import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavOptions
import dagger.hilt.android.AndroidEntryPoint
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.viewModel.HomeViewModel
import se.example.mushroommapper.viewModel.MapViewModel


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
            val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)
            val mapViewModel = viewModel(modelClass = MapViewModel::class.java)
            //val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)
            MushroomMapperTheme {
                RootNavigationGraph(
                    navController = rememberNavController(),
                    detailViewModel = detailViewModel,
                    homeViewModel = homeViewModel,
                    mapViewModel = mapViewModel
                )
                /*Navigation(
                    detailViewModel = detailViewModel,
                    homeViewModel = homeViewModel
                )*/
            }
        }
    }
}
/*
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MushroomMapperTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

*/
@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MushroomMapperTheme {
        Greeting("Android")
    }
}