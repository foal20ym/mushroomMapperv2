package se.example.mushroommapper


import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import org.koin.androidx.viewmodel.ext.android.viewModel
import se.example.mushroommapper.detail.DetailViewModel
import se.example.mushroommapper.navigation.RootNavigationGraph
import se.example.mushroommapper.ui.theme.MushroomMapperTheme
import se.example.mushroommapper.viewModel.HomeViewModel
import se.example.mushroommapper.viewModel.LocationViewModel
import se.example.mushroommapper.viewModel.MapViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationViewModel : LocationViewModel by viewModel<LocationViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val homeViewModel = viewModel(modelClass = HomeViewModel::class.java)
            val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)
            val mapViewModel = viewModel(modelClass = MapViewModel::class.java)
            val locationViewModel = viewModel(modelClass = LocationViewModel::class.java)
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            //val detailViewModel = viewModel(modelClass = DetailViewModel::class.java)
            MushroomMapperTheme {
                RootNavigationGraph(
                    navController = rememberNavController(),
                    detailViewModel = detailViewModel,
                    homeViewModel = homeViewModel,
                    mapViewModel = mapViewModel
                )
                prepLocationUpdates()
            }
        }
    }
    private fun prepLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
            requestLocationUpdates()
        } else {
            requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private val requestSinglePermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            isGranted ->
        if (isGranted) {
            requestLocationUpdates()
        } else {
            Toast.makeText(this, "GPS Unavailable", Toast.LENGTH_LONG).show()
        }
    }

    private fun requestLocationUpdates() {
        locationViewModel.startLocationUpdates()
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