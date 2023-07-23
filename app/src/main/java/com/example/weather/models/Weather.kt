package com.example.weather.models

data class Weather(
    val cityName:String,
    val updatedTime :String,
    val tempCurrent:String,
    val contitionText:String,
    val imageIcon:String,
    val tempMin:String,
    val tempMax:String,
    val hoursWeather:String,
    val averageTemp:String,
    )
