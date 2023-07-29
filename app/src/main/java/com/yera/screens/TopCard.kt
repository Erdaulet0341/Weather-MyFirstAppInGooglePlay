package com.yera.screens

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yera.models.Weather
import com.yera.theme.Blue50
import com.yera.theme.DayBtn
import com.yera.theme.Text_color
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode

const val google_api = "AIzaSyCl5L0KhMeMi1VIDXse5lUhXN9tlpB-_FE"
@Composable
fun TopCard(
    currentDay: MutableState<Weather>,
    refresh: () -> Unit,
    search: () -> Unit,
    searchCity: MutableState<String>,
) {

    val context = LocalContext.current

    val intentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                it.data?.let {
                    val place = Autocomplete.getPlaceFromIntent(it)
                    val startIdx = place.latLng.toString().indexOf("(") + 1
                    val endIdx =  place.latLng.toString().indexOf(")")
                    val latLngStr =  place.latLng.toString().substring(startIdx, endIdx)
                    searchCity.value = latLngStr
                    search.invoke()
                    Log.i("MAP_ACTIVITY", "Place: ${place.name}, ${latLngStr}")
                }
            }
            Activity.RESULT_CANCELED -> {
                it.data?.let {
                    val status = Autocomplete.getStatusFromIntent(it)
                    Log.i("MAP_ACTIVITY", "Error: ${status.statusCode}")
                }            }
        }
    }

    val launchMapInputOverlay = {
        Places.initialize(context, google_api)
        val fields = listOf(Place.Field.NAME, Place.Field.LAT_LNG)
        val intent = Autocomplete
            .IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
            .build(context)
        intentLauncher.launch(intent)
//        isLoading.value = true
    }

    Column(
        modifier = Modifier.padding(5.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(
                containerColor = Blue50
            ), elevation = CardDefaults.cardElevation(
                defaultElevation = 0.dp
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(top = 7.dp, start = 5.dp),
                        text = currentDay.value.updatedTime,
                        fontSize = 15.sp,
                        color = DayBtn,
                        fontFamily = FontFamily.Serif
                    )
                    AsyncImage(
                        model = "https:${currentDay.value.imageIcon}",
                        contentDescription = "image",
                        modifier = Modifier
                            .size(45.dp)
                            .padding(top = 3.dp, end = 7.dp)
                    )
                }
                Text(
                    text = currentDay.value.cityName,
                    fontSize = 24.sp,
                    color = Text_color,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text =
                    if (currentDay.value.tempCurrent.isNotEmpty())
                        "${currentDay.value.tempCurrent.toFloat().toInt()}\u2103"
                    else "${currentDay.value.averageTemp.toFloat().toInt()}℃",
                    fontSize = 60.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = currentDay.value.contitionText,
                    fontSize = 16.sp,
                    color = Text_color,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = launchMapInputOverlay,
                        modifier = Modifier.size(45.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "image",
                            modifier = Modifier.size(30.dp),
                            tint = Color.White
                        )
                    }
                    Text(
                        text = "${
                            currentDay.value.tempMin.toFloat().toInt()
                        }℃/${currentDay.value.tempMax.toFloat().toInt()}℃",
                        fontSize = 16.sp,
                        color = DayBtn,
                        modifier = Modifier.padding(top = 10.dp),
                        fontFamily = FontFamily.Serif
                    )
                    IconButton(
                        onClick = {
                            refresh.invoke()
                        }, modifier = Modifier.size(45.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "image",
                            modifier = Modifier.size(30.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}
