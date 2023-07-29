package com.yera

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.yera.models.Weather
import com.yera.screens.Screen
import com.yera.theme.WeatherTheme
import org.json.JSONObject
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

const val API_KEY = "b6ab9133c45b4f62aef43930231907"

class MainActivity : ComponentActivity() {
    private lateinit var locationManager: LocationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
                Surface(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
                ) {
                    val listDays = remember {
                        mutableStateOf(listOf<Weather>())
                    }
                    val isLoading = remember {
                        mutableStateOf(true)
                    }
                    val checkLaunch = remember {
                        mutableStateOf(true)
                    }
                    val checkLocation = remember {
                        mutableStateOf(false)
                    }
                    val searchCity = remember {
                        mutableStateOf("")
                    }
                    val currentDay = remember {
                        mutableStateOf(Weather("", "", "0.0", "", "", "0.0", "0.0", "", "0.0"))
                    }
                    if (checkLaunch.value) {
                        PermissionAwareScreen(
                            context = this@MainActivity,
                            listDays,
                            currentDay,
                            isLoading,
                            checkLocation
                        )
                    }

                    LaunchedEffect(Unit) {
                        checkLaunch.value = false
                    }

                    Screen(
                        listDays, currentDay,
                        refresh = {
                            isLoading.value = true
                            checkLocation.value = true
                        },
                        search = {
                            isLoading.value = true
                            getResult(
                                searchCity.value,
                                this@MainActivity,
                                listDays,
                                currentDay,
                                isLoading
                            )
                        },
                        searchCity = searchCity,
                    )

                    if (isLoading.value) {
                        CircularProgressIndicator(
                            modifier = Modifier.scale(0.17f),
                            color = Color.White,
                            strokeWidth = 22.dp
                        )
                    }

                    if (checkLocation.value) {
                        PermissionAwareScreen(
                            context = this@MainActivity,
                            listDays,
                            currentDay,
                            isLoading,
                            checkLocation
                        )
                    }
                }
            }
        }
    }
}

private lateinit var fusedLocation: FusedLocationProviderClient

@RequiresApi(Build.VERSION_CODES.O)
private fun getLocation(
    context: Context,
    listDays: MutableState<List<Weather>>,
    currentDay: MutableState<Weather>,
    isLoading: MutableState<Boolean>
) {
    if (!isGPS(context)) {
        return
    }
    if (ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }
    fusedLocation = LocationServices.getFusedLocationProviderClient(context)
    fusedLocation.getCurrentLocation(
        Priority.PRIORITY_HIGH_ACCURACY,
        CancellationTokenSource().token
    ).addOnCompleteListener {
        val s = it.result.latitude.toString() + "," + it.result.longitude.toString()
        getResult(s, context, listDays, currentDay, isLoading)
    }.addOnFailureListener {
        Toast.makeText(context, "Error when getting location", Toast.LENGTH_SHORT).show()
    }
}

private fun isGPS(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
}

private fun isNetwork(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                ?: false
        }
        return true
    }
    return false
}

private fun isPermissionGranted(name: String, context: Context): Boolean {
    return ContextCompat
        .checkSelfPermission(context, name) ==
            PackageManager.PERMISSION_GRANTED
}

@Composable
fun PermissionHandler(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionDeniedForever: () -> Unit,
    context2: Context,
    isPermission: MutableState<Boolean>
) {
    Log.d("one1", "nnn")
    val context = LocalContext.current
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onPermissionGranted()
                Log.d("one2", "nnn")
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        permission
                    )

                ) {
                    onPermissionDenied()
                    isPermission.value = false
                    Log.d("one4", "nnn")

                } else {
                    onPermissionDeniedForever()
                    Log.d("one5", "nnn")

                }
            }
        }

    LaunchedEffect(permission) {
        if (!isPermissionGranted(permission, context)) {
            launcher.launch(permission)
            isPermission.value = false
        } else {
            Log.d("one3", "nnn")
            onPermissionGranted()
        }
    }
}


@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PermissionAwareScreen(
    context: Context,
    listDays: MutableState<List<Weather>>,
    currentDay: MutableState<Weather>,
    isLoading: MutableState<Boolean>,
    checkLocation: MutableState<Boolean>
) {
    val permission = remember {
        mutableStateOf(false)
    }
    val gps = remember {
        mutableStateOf(false)
    }

    gps.value = isGPS(context)

    PermissionHandler(
        permission = Manifest.permission.ACCESS_FINE_LOCATION,
        onPermissionGranted = {
            permission.value = true
            if (isGPS(context)) {
                getLocation(context, listDays, currentDay, isLoading)
                checkLocation.value = false
            }
        },
        onPermissionDenied = {
            permission.value = false
        },
        onPermissionDeniedForever = {
            permission.value = false
        },
        context,
        permission
    )

    if (!isGPS(context)) {
        isLoading.value = false
        getResult("Almaty", context, listDays, currentDay, isLoading)
        Toast.makeText(context, "Please turn on your Location, and try again", Toast.LENGTH_SHORT)
            .show()
    }

    if (!isNetwork(context)) {
        isLoading.value = false
        Toast.makeText(
            context,
            "Please turn on your Internet connection, and try again",
            Toast.LENGTH_SHORT
        ).show()
    }

    if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION, context)) {
        getResult("Almaty", context, listDays, currentDay, isLoading)
        Toast.makeText(context, "Please give location permission", Toast.LENGTH_SHORT).show()
    }

}

@RequiresApi(Build.VERSION_CODES.O)
private fun getResult(
    name: String,
    context: Context,
    listDays: MutableState<List<Weather>>,
    currentDay: MutableState<Weather>,
    isLoading: MutableState<Boolean>
) {
    val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
            "$API_KEY&" +
            "q=$name" +
            "&days=10" +
            "&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        { response ->
            val list = byDays(response, isLoading, context)
            listDays.value = list
            currentDay.value = list[0]
        },
        { error ->
            Toast.makeText(
                context,
                "Please turn on your Internet connection, and try again",
                Toast.LENGTH_SHORT
            ).show()
            isLoading.value = false
        }
    )
    queue.add(stringRequest)
}

@RequiresApi(Build.VERSION_CODES.O)
private fun byDays(
    response: String,
    isLoading: MutableState<Boolean>,
    context: Context
): List<Weather> {
    if (response.isEmpty()) {
        Toast.makeText(context, "Undefined error, please try again!", Toast.LENGTH_SHORT).show()
        return listOf()
    }

    val list = ArrayList<Weather>()

    val jsonObj = JSONObject(response)
    val days = jsonObj.getJSONObject("forecast").getJSONArray("forecastday")

    for (i in 0 until days.length()) {
        val item = days[i] as JSONObject
        val timeString = item.getString("date")

        val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val originalDate = LocalDate.parse(timeString, originalFormat)
        val outputFormat = DateTimeFormatter.ofPattern("d MMMM", Locale.ENGLISH)
        val formattedDate = originalDate.format(outputFormat)

        list.add(
            Weather(
                jsonObj.getJSONObject("location").getString("name"),
                formattedDate,
                "",
                item.getJSONObject("day").getJSONObject("condition").getString("text"),
                item.getJSONObject("day").getJSONObject("condition").getString("icon"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONArray("hour").toString(),
                item.getJSONObject("day").getString("avgtemp_c")
            )
        )
    }

    val timeString = jsonObj.getJSONObject("location").getString("localtime")
    val originalFormat: DateTimeFormatter?
    if (timeString.length == 15) {
        originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd H:mm")
    } else {
        originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    }
    val originalDateTime = LocalDateTime.parse(timeString, originalFormat)
    val outputFormat = DateTimeFormatter.ofPattern("d MMMM HH:mm", Locale.ENGLISH)
    val formattedDateTime = originalDateTime.format(outputFormat)

    list[0] = list[0].copy(
        updatedTime = formattedDateTime,
        tempCurrent = jsonObj.getJSONObject("current").getString("temp_c")
    )
    isLoading.value = false

    return list
}