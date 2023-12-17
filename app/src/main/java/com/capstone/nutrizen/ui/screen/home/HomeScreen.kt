package com.capstone.nutrizen.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.helper.calculateBMI
import com.capstone.nutrizen.helper.calculateBMR
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    ),
) {
    var name =""
    var age = 0
    var genderId=0
    var height=0.0
    var weight=0.0
    var activityId=0
    var goal=0
    var bmi = 0.0
    var bmr = 0.0
    var calorieNeeds = 0.0
    viewModel.getSession().observeAsState().value?.let { its ->
        name = its.name
        age = its.age
        genderId = its.gender
        height = its.height
        weight = its.weight
        activityId = its.activity
        goal = its.goal
    }
    var activity: Double =
        when (activityId) {
            1 -> 1.2
            2 -> 1.375
            3 -> 1.550
            4 -> 1.725
            5 -> 1.9
            else -> 0.0
        }
    var gender: Int =
        when (genderId) {
            1 -> 5
            2 -> -161
            else -> 0
        }
    bmi = calculateBMI(height,weight)
    var bmiDesc: String =""
    bmr = calculateBMR(weight,height,age,gender,activity)
    calorieNeeds=
        when(goal) {
            1 -> bmr *4/5
            3-> bmr +500
            else-> bmr
        }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "Hi, ${name}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Start
                )
            }
            ,colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.primaryContainer)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(20.dp)
                    .height(100.dp),
            ) {
                Text(
                    text = "Your daily Calories goal",
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .align(Alignment.TopCenter),
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Start
                )
                Text(
                    text = round(calorieNeeds).toString(),
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .align(Alignment.BottomStart),
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Calories",
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .align(Alignment.BottomEnd),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
