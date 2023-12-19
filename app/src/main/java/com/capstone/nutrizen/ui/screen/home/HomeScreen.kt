package com.capstone.nutrizen.ui.screen.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.helper.calculateBMI
import com.capstone.nutrizen.helper.calculateBMR
import com.capstone.nutrizen.helper.toSimpleString
import com.capstone.nutrizen.ui.common.UiState
import com.capstone.nutrizen.ui.components.Summary
import java.util.Date
import kotlin.math.round
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    ),
) {
    var token = ""
    var id = ""
    val currentDate = Date().toSimpleString()
    var name = ""
    var age = 0
    var genderId = 0
    var height = 0.0
    var weight = 0.0
    var activityId = 0
    var goalId = 0
    var bmi = 0.0
    var bmr = 0.0
    var calorieNeeds = 0.0
    var calsConsumed = 0
    var calsRemaining = 0
    viewModel.getSession().observeAsState().value?.let { its ->
        token = its.token
        id = its.id
        name = its.name
        age = its.age
        genderId = its.gender
        height = its.height
        weight = its.weight
        activityId = its.activity
        goalId = its.goal
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
    var goal: String =
        when (goalId) {
            1 -> "Lose Weight"
            2 -> "Maintain Weight"
            3 -> "Gain Weight"
            else -> "empty"
        }
    bmr = calculateBMR(weight, height, age, gender, activity)
    calorieNeeds =
        when (goalId) {
            1 -> bmr * 4 / 5
            3 -> bmr + 500
            else -> bmr
        }

    bmi = calculateBMI(height, weight)
    var bmiDesc = if (bmi < 18.5) {
        "Underweight"
    } else if (bmi > 25 && bmi <= 30) {
        "Overweight"
    } else if (bmi > 30) {
        "Obese"
    } else {
        "Normal"
    }

    viewModel.getHistory(token, id, currentDate)
    viewModel.uiStates.collectAsState().value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getHistory(token, id, currentDate)
            }

            is UiState.Success -> {
                uiState.data.forEach { calsConsumed += it.total }
            }

            is UiState.Error -> {
                val TAG = "errors"
                Log.d(TAG, "onFAILED: ${uiState.copy()}")
            }
        }
    }

    Column(
        modifier = modifier.fillMaxWidth()
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
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.primaryContainer)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 10.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(10.dp)
            ) {
                Text(
                    text = "My Body Mass Index (BMI) score",
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
                        text = round(bmi).toString(),
                        modifier = Modifier
                            .padding(horizontal = 15.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = bmiDesc,
                        modifier = Modifier
                            .padding(horizontal = 12.dp),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    color = Color.Gray,
                    thickness = 2.dp
                )

                Row(
                    modifier = Modifier.padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = "My Goal is to ",
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic,
                    )
                    Text(
                        text = goal,
                        modifier = Modifier,
                        fontWeight = FontWeight.SemiBold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            if (calsConsumed > 0) {
                calsRemaining = calorieNeeds.roundToInt() - calsConsumed
                if (calsRemaining > 0) {
                    var calsRemainingPercent: Float =
                        ((calsRemaining / calorieNeeds) * 1000).toFloat()
                    var calsConsumedPercent: Float =
                        ((calsConsumed / calorieNeeds) * 1000).toFloat()
                    var percent = ((calsConsumed / calorieNeeds) * 100).roundToInt()
                    Summary(
                        calsGoal = calorieNeeds.roundToInt(),
                        calsConsumed = calsConsumed,
                        calsRemaining = calsRemaining,
                        calsRemainingPercent = calsRemainingPercent,
                        calsConsumedPercent = calsConsumedPercent,
                        percent = percent
                    )
                }else if(calsRemaining==0){
                    Summary(
                        calsGoal = calorieNeeds.roundToInt(),
                        calsConsumed = calsConsumed,
                        calsRemaining = calsRemaining,
                        calsRemainingPercent = 0f,
                        calsConsumedPercent =100f,
                        percent = 100
                    )
                }else{
                    var percent = ((calsConsumed / calorieNeeds) * 100).roundToInt()
                    Summary(
                        calsGoal = calorieNeeds.roundToInt(),
                        calsConsumed = calsConsumed,
                        calsRemaining = calsRemaining,
                        calsRemainingPercent = 0f,
                        calsConsumedPercent = 100f,
                        percent = percent
                    )
                }
            } else {
                Summary(
                    calsGoal = calorieNeeds.roundToInt(),
                    calsConsumed = 0,
                    calsRemaining = calorieNeeds.roundToInt(),
                    calsRemainingPercent = 100f,
                    calsConsumedPercent = 0f,
                    percent = 0
                )
            }
        }
    }
}
