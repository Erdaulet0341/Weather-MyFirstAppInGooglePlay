package com.example.weather.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun SearchCity(dialogState:MutableState<Boolean>, searchBtn: (String)->Unit){
    val searchText = remember {
        mutableStateOf("")
    }
    AlertDialog(onDismissRequest = {
            dialogState.value = false
    },
        confirmButton = {
            TextButton(onClick = {
                searchBtn(searchText.value)
                dialogState.value = false
            }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                dialogState.value = false
            }) {
                Text(text = "Cancel")
            }
        },
        title = {
            Column(modifier =  Modifier.fillMaxWidth()) {
                Text(text = "Search here...")
                TextField(value = searchText.value, onValueChange = {
                    searchText.value = it
                })
            }
        }
    )
}