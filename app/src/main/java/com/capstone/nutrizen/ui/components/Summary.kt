package com.capstone.nutrizen.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.nutrizen.R
import io.jetchart.common.animation.fadeInAnimation
import io.jetchart.pie.PieChart
import io.jetchart.pie.Pies
import io.jetchart.pie.Slice
import io.jetchart.pie.renderer.FilledSliceDrawer


@Composable
fun Summary(
    calsGoal: Int,
    calsConsumed: Int,
    calsRemaining: Int,
    calsRemainingPercent: Float,
    calsConsumedPercent: Float,
    percent: Int,
    modifier: Modifier = Modifier,
) {

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
            text = "Summary",
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp, vertical = 7.dp),
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            textAlign = TextAlign.Start
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            PieChart(
                pies = Pies(
                    listOf(
                        Slice(calsConsumedPercent, colorResource(id = R.color.greens)),
                        Slice(calsRemainingPercent, colorResource(id = R.color.oranges)),
                    )
                ),
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp),
                animation = fadeInAnimation(4000),
                sliceDrawer = FilledSliceDrawer(thickness = 50f)
            )
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$percent%",
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "of $calsGoal",
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                    fontWeight = FontWeight.Normal,
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = "Calories Goal",
                    modifier = Modifier
                        .padding(vertical = 2.dp),
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Icon(imageVector = Icons.Default.Circle, tint = colorResource(id = R.color.greens), contentDescription = null)
                Text(
                    text = "Calories Consumed",
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = calsConsumed.toString(),
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

        }
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                Icon(imageVector = Icons.Default.Circle, tint = colorResource(id = R.color.oranges), contentDescription = null)
                Text(
                    text = "Calories Remaining",
                    modifier = Modifier
                        .padding(horizontal = 10.dp),
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center
                )
            }
            Text(
                text = calsRemaining.toString(),
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                fontWeight = FontWeight.Normal,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
    }
}