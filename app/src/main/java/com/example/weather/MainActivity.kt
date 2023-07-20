package com.example.weather

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weather.models.Weather
import com.example.weather.screens.Screen
import com.example.weather.screens.SearchCity
import com.example.weather.ui.theme.WeatherTheme
import org.json.JSONObject

const val API_KEY = "b6ab9133c45b4f62aef43930231907"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val listDays = remember {
                        mutableStateOf(listOf<Weather>())
                    }
                    val dialog = remember {
                        mutableStateOf(false)
                    }
                    val currentDay = remember {
                        mutableStateOf(Weather("","","0.0","","","0.0","0.0",""))
                    }

                    if(dialog.value){
                        SearchCity(dialog, searchBtn = {
                            getResult(it, this@MainActivity, listDays, currentDay)
                        })
                    }

                    getResult("Almaty", this, listDays, currentDay)
                    Screen(listDays, currentDay, refresh = {
                        getResult("Almaty", this@MainActivity, listDays, currentDay)
                    }, search = {
                        dialog.value = true
                    }
                    )
                }
            }
        }
    }
}

private fun getResult(
    name: String,
    context: Context,
    listDays: MutableState<List<Weather>>,
    currentDay: MutableState<Weather>
){
    val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
            "$API_KEY&" +
            "q=$name" +
            "&days=10"+
            "&aqi=no&alerts=no"
    val queue = Volley.newRequestQueue(context)
    val stringRequest = StringRequest(
        Request.Method.GET,
        url,
        {
            response ->
            val list = byDays(response)
            listDays.value = list
            currentDay.value = list[0]
        },
        {
            error ->
            Log.d("errror", error.toString())
        }
    )
    queue.add(stringRequest)
}

private fun byDays(response:String):List<Weather>{
    if(response.isNullOrEmpty()) return listOf()

    val list = ArrayList<Weather>()

    val jsonObj = JSONObject(response)
    val days = jsonObj.getJSONObject("forecast").getJSONArray("forecastday")

    for(i in 0 until days.length()){
        val item = days[i] as JSONObject
        list.add(
            Weather(
                jsonObj.getJSONObject("location").getString("name"),
                item.getString("date"),
                "",
                item.getJSONObject("day").getJSONObject("condition").getString("text"),
                item.getJSONObject("day").getJSONObject("condition").getString("icon"),
                item.getJSONObject("day").getString("mintemp_c"),
                item.getJSONObject("day").getString("maxtemp_c"),
                item.getJSONArray("hour").toString()
                )
        )
    }

    list[0] = list[0].copy(
        updatedTime = jsonObj.getJSONObject("current").getString("last_updated"),
        tempCurrent = jsonObj.getJSONObject("current").getString("temp_c")
    )

    return  list
}