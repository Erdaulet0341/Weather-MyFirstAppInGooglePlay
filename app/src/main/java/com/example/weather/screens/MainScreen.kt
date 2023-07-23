package com.example.weather.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.example.weather.models.Weather
import com.example.weather.ui.theme.Bg_color

@Composable
fun Screen(
    listDays: MutableState<List<Weather>>,
    currentDay: MutableState<Weather>,
    refresh: () -> Unit,
    search: () -> Unit,
    searchCity: MutableState<String>,
) {
    Surface(modifier = Modifier.fillMaxSize(),
        color = Bg_color,
    ) {
        Column {
            TopCard(currentDay, refresh, search, searchCity)
            TabLayout(listDays, currentDay)
        }
    }

//    Image(
//        painter = painterResource(id = R.drawable.weather_bg),
//        contentDescription = "bg",
//        modifier = Modifier
//            .fillMaxSize()
//            .alpha(0.6f),
//        contentScale = ContentScale.FillBounds
//    )
//    Column {
//        TopCard(currentDay, refresh, search)
//        TabLayout(listDays, currentDay)
//    }
}
