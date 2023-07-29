package com.yera.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.yera.models.Weather
import com.yera.theme.Blue50
import com.yera.theme.Text_color
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabLayout(listDays: MutableState<List<Weather>>, currentDay: MutableState<Weather>) {
    val tapList = listOf("HOURS", "DAYS")
    val pagerState = rememberPagerState()
    val tabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .clip(RoundedCornerShape(5.dp))
    ) {
        TabRow(
            selectedTabIndex = tabIndex,
            indicator = { pos ->
                TabRowDefaults.Indicator(
                    Modifier.pagerTabIndicatorOffset(pagerState, pos)
                )
            },
            contentColor = Text_color,
            backgroundColor = Blue50,
        ) {
            tapList.forEachIndexed { index, name ->
                Tab(
                    selected = false,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(text = name,
                            color = Text_color,
                            fontFamily = FontFamily.Serif)
                    },

                    )
            }
        }
        HorizontalPager(
            count = tapList.size, state = pagerState, modifier = Modifier.weight(1.0f)
        ) { index ->
            val list = when(index){
                0 -> byHours(currentDay.value.hoursWeather)
                1 -> listDays.value
                else -> listDays.value
            }
            ListSetup(list = list, currentDay = currentDay)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun byHours(hours: String):List<Weather>{
    if(hours.isNullOrEmpty()) return listOf()
    val list = ArrayList<Weather>()
    val jsonArray = JSONArray(hours)

    for(i in 0 until jsonArray.length()){
        val item = jsonArray[i] as JSONObject
        val timeString = item.getString("time")

        val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val originalDateTime = LocalDateTime.parse(timeString, originalFormat)
        val outputFormat = DateTimeFormatter.ofPattern("d MMMM HH:mm", Locale.ENGLISH)
        val formattedDateTime = originalDateTime.format(outputFormat)

        list.add(
            Weather(
                "",
                formattedDateTime,
                item.getString("temp_c").toFloat().toInt().toString() + "℃",
                item.getJSONObject("condition").getString("text"),
                item.getJSONObject("condition").getString("icon"),
                "",
                "",
                "",
                ""
            )
        )
    }

    return list
}