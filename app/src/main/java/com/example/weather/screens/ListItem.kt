package com.example.weather.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.models.Weather
import com.example.weather.ui.theme.Blue50


@Composable
fun ListItem(weather: Weather, currentDay: MutableState<Weather>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .clickable {
                if(weather.hoursWeather.isEmpty()) return@clickable
                currentDay.value = weather
            },
            colors = CardDefaults.cardColors(
            containerColor = Blue50
        ), elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ), shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 7.dp,
                    top = 5.dp,
                    bottom = 5.dp
                )
            ) {
                Text(text = weather.updatedTime)
                Text(
                    text = weather.contitionText,
                    color = Color.White
                )
            }
            Text(
                text = weather.tempCurrent.ifEmpty { "${weather.tempMin.toFloat().toInt()}℃/${weather.tempMax.toFloat().toInt()}℃" },
                color = Color.White,
                fontSize = 23.sp
            )
            AsyncImage(
                model = "https:${weather.imageIcon }",
                contentDescription = "image",
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(35.dp)
            )
        }
    }
}
