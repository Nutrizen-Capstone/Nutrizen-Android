package com.capstone.nutrizen.ui.screen.scan

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.nutrizen.R
import com.capstone.nutrizen.activity.scan.ScanActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(
    context: Context,
    activity: ComponentActivity,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.title_scan),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.ImageSearch,
                contentDescription = "icon",
                modifier = modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(vertical = 10.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )

            Text(
                text = stringResource(id = R.string.scan_description),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = { context.startActivity(Intent(context, ScanActivity::class.java)) },
                modifier = Modifier.width(300.dp)
            )
            {
                Text(text = stringResource(id = R.string.btn_toScan))
            }
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(id = R.string.scan_alert),
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Start
            )
            Box(
                modifier = Modifier
                    .padding(10.dp)
                    .background(color = MaterialTheme.colorScheme.secondaryContainer, shape = MaterialTheme.shapes.small)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = stringResource(id = R.string.scan_list),
                        fontSize = 15.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }
        }
    }
}
