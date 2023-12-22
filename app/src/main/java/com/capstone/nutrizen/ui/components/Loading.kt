package com.capstone.nutrizen.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.nutrizen.R

@Composable
fun Loading(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        Column(  modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_nutrizen),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clip(CircleShape),
                    contentDescription = "logo"
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = TextStyle(fontSize = 50.sp, fontFamily = FontFamily.Serif,),
                    color = colorResource(id = R.color.greens),
                )
            }
            Text(
                text = stringResource(id = R.string.loading),
                modifier = Modifier
                    .padding(15.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
            )
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = colorResource(id = R.color.greens),
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}