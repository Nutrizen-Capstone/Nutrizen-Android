package com.capstone.nutrizen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun HistoryItem(
    food: String,
    time: String,
    date: String,
    cals: Int,
    portion: Double,
    total: Int,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier) {
                Icon(
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp),
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = null
                )
                Text(text = time, fontSize = 15.sp)
            }
            Row(modifier = Modifier) {
                Icon(
                    modifier = Modifier
                        .height(20.dp)
                        .width(20.dp),
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = null
                )
                Text(text = date, fontSize = 15.sp)
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            color = Color.Gray
        )

        Row {
            Icon(
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp),
                imageVector = Icons.Default.Fastfood,
                contentDescription = null
            )
            Text(text = food, fontSize = 20.sp)
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            color = Color.Gray,
            thickness = 5.dp
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Calories/100g", fontStyle= FontStyle.Italic)
            Text(text = cals.toString())
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Serving", fontStyle= FontStyle.Italic, )
            Text(text = "$portion x")
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 2.dp),
            color = Color.Gray,
            thickness = 3.dp
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Text(text = "Total Calories", fontStyle= FontStyle.Italic, )
            Text(text = total.toString())
        }

    }
}