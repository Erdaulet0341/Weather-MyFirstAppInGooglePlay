package com.example.weather.screens

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weather.models.Weather
import com.example.weather.ui.theme.Blue50


@Composable
fun TopCard(currentDay: MutableState<Weather>, refresh: () -> Unit, search: () -> Unit) {
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
                        modifier = Modifier.padding(top = 7.dp),
                        text = currentDay.value.updatedTime,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                    AsyncImage(
                        model = "https:${currentDay.value.imageIcon}",
                        contentDescription = "image",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(top = 3.dp, end = 7.dp)
                    )
                }
                Text(
                    text = currentDay.value.cityName,
                    fontSize = 24.sp,
                    color = Color.White
                )
                Text(
                    text =
                    if (currentDay.value.tempCurrent.isNotEmpty())
                        "${currentDay.value.tempCurrent.toFloat().toInt()} \u2103"
                    else "${
                        currentDay.value.tempMin.toFloat().toInt()
                    }℃/${currentDay.value.tempMax.toFloat().toInt()}℃",
                    fontSize = 65.sp,
                    color = Color.White
                )
                Text(
                    text = currentDay.value.contitionText,
                    fontSize = 16.sp,
                    color = Color.White
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            search.invoke()
                        }, modifier = Modifier.size(45.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "image",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    Text(
                        text = "${
                            currentDay.value.tempMin.toFloat().toInt()
                        }℃/${currentDay.value.tempMax.toFloat().toInt()}℃",
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                    IconButton(
                        onClick = {
                            refresh.invoke()
                        }, modifier = Modifier.size(45.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "image",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }
    }
}
