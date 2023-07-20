package com.example.weather.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.weather.R
import com.example.weather.models.Weather

@Composable
fun Screen(listDays: MutableState<List<Weather>>, currentDay: MutableState<Weather>,  refresh: () ->Unit, search: () ->Unit) {
    Image(
        painter = painterResource(id = R.drawable.weather_bg),
        contentDescription = "bg",
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.6f),
        contentScale = ContentScale.FillBounds
    )
    Column {
        TopCard(currentDay, refresh, search)
        TabLayout(listDays, currentDay)
    }
}
