package com.capstone.nutrizen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.jetchart.common.animation.fadeInAnimation
import io.jetchart.pie.PieChart
import io.jetchart.pie.Pies
import io.jetchart.pie.Slice
import io.jetchart.pie.renderer.FilledSliceDrawer


@Composable
fun Summary(
    calsGoal: Int,
    calsConsumed: Int,
    modifier: Modifier = Modifier,
) {
    var calsRemaining = calsGoal-calsConsumed
    var calsRemainingPercent = ((calsRemaining/calsGoal)*100).toFloat()
    var calsConsumedPercent = ((calsConsumed/calsGoal)*100).toFloat()

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            )
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "My Calories Goal",
            modifier = Modifier
                .padding(horizontal = 12.dp),
            fontWeight = FontWeight.Normal,
            fontSize = 17.sp,
            textAlign = TextAlign.Start
        )
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = calsGoal.toString(),
                modifier = Modifier
                    .padding(horizontal = 15.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                textAlign = TextAlign.Center,

                )
            Text(
                text = "Calories",
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            PieChart(
                pies = Pies(
                    listOf(
                        Slice(calsConsumedPercent, Color.Blue),
                        Slice(calsRemainingPercent, Color.Yellow),
                    )
                ),
                modifier = Modifier,
                animation = fadeInAnimation(4000),
                sliceDrawer = FilledSliceDrawer(thickness = 60f)
            )
            Text(
                text = calsConsumed.toString(),
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
    }

}