package com.example.weather.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.example.weather.models.Weather

@Composable
fun ListSetup(list: List<Weather>, currentDay: MutableState<Weather>){
    LazyColumn(modifier = Modifier.fillMaxSize()){
        itemsIndexed(
            list
        ){
                _, item ->
            ListItem(weather = item, currentDay)
        }
    }
}