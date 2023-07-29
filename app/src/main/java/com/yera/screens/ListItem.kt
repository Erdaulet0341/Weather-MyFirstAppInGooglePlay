package com.yera.screens

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.yera.models.Weather
import com.yera.theme.Blue50
import com.yera.theme.DayBtn
import com.yera.theme.TempColor
import com.yera.theme.Text_color


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
                Text(text = weather.updatedTime,
                    color = DayBtn,
                    fontFamily = FontFamily.Serif)
                Text(
                    modifier = Modifier.fillMaxWidth(0.37f),
                    text = weather.contitionText,
                    color = Text_color,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic
                )
            }
            Text(
                text = weather.tempCurrent.ifEmpty { "${weather.tempMin.toFloat().toInt()}℃/${weather.tempMax.toFloat().toInt()}℃" },
                color = TempColor,
                fontSize = 19.sp,
                fontFamily = FontFamily.Serif
            )
            AsyncImage(
                model = "https:${weather.imageIcon }",
                contentDescription = "image",
                modifier = Modifier
                    .padding(end = 5.dp)
                    .size(40.dp)
            )
        }
    }
}
