package com.yera.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.yera.models.Weather
import com.yera.theme.bgg

@Composable
fun Screen(
    listDays: MutableState<List<Weather>>,
    currentDay: MutableState<Weather>,
    refresh: () -> Unit,
    search: () -> Unit,
    searchCity: MutableState<String>,
) {
    Surface(modifier = Modifier.fillMaxSize(),
        color = bgg,
    ) {
        Column {
            TopCard(currentDay, refresh, search, searchCity)
            TabLayout(listDays, currentDay)
        }
    }
}
