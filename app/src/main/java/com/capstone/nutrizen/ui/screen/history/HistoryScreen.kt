package com.capstone.nutrizen.ui.screen.history

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.ManageSearch
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.nutrizen.R
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.data.retrofit.response.HistoryItem
import com.capstone.nutrizen.helper.calculateBMR
import com.capstone.nutrizen.helper.toSimpleString
import com.capstone.nutrizen.ui.common.UiState
import com.capstone.nutrizen.ui.components.HistoryItem
import com.capstone.nutrizen.ui.components.Summary
import es.dmoral.toasty.Toasty
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    context: Context,
    activity: ComponentActivity,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    ),
) {

    var token = ""
    var id = ""
    var age = 0
    var genderId = 0
    var height = 0.0
    var weight = 0.0
    var activityId = 0
    var goalId = 0
    var bmr = 0.0
    var calorieNeeds = 0.0
    viewModel.getSession().observeAsState().value?.let { its ->
        token = its.token
        id = its.id
        age = its.age
        genderId = its.gender
        height = its.height
        weight = its.weight
        activityId = its.activity
        goalId = its.goal
    }
    var activitys: Double =
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
    bmr = calculateBMR(weight, height, age, gender, activitys)
    calorieNeeds =
        when (goalId) {
            1 -> bmr * 4 / 5
            3 -> bmr + 500
            else -> bmr
        }
    val currentDate = Date().toSimpleString()

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.title_history),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        )
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed: Boolean by interactionSource.collectIsPressedAsState()
        var selectedDate = remember { mutableStateOf(currentDate) }
        val calendar = Calendar.getInstance()
        val year: Int = calendar.get(Calendar.YEAR)
        val month: Int = calendar.get(Calendar.MONTH)
        val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.time = Date()
        val datePickerDialog =
            DatePickerDialog(
                context,
                { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, month, dayOfMonth)
                    selectedDate.value = "$year-${month + 1}-$dayOfMonth"
                    viewModel.getHistory(token, id, selectedDate.value)
                },
                year,
                month,
                day
            )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ManageSearch,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = selectedDate.value,
                    onValueChange = {},
                    trailingIcon = { Icons.Default.DateRange },
                    interactionSource = interactionSource,
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_search),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors()
                )
            }
            if (isPressed) {
                datePickerDialog.show()
            }
        }
        Spacer(modifier = Modifier.height(15.dp))

        viewModel.getHistory(token, id, selectedDate.value)
        viewModel.uiStates.collectAsState().value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    viewModel.getHistory(token, id, selectedDate.value)
                }

                is UiState.Success -> {
                    if (uiState.data.error) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {

                            Column(
                                modifier = Modifier
                                    .height(200.dp)
                                    .width(200.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        shape = MaterialTheme.shapes.medium
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .height(150.dp)
                                        .width(150.dp),
                                    imageVector = Icons.Default.SearchOff,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary
                                )
                                Text(text = "Data Not Found", fontSize = 15.sp)

                            }
                        }
                    } else
                        HistoryContent(list = uiState.data.history,calorieNeeds, token, id, context, activity)
                }

                is UiState.Error -> {
                    val TAG = "errors"
                    Log.d(TAG, "onFAILED: ${uiState.copy()}")
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(onClick = {
                            viewModel.getHistory(token, id, selectedDate.value)
                        }) {
                            Text(text = "retry")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryContent(
    list: List<HistoryItem>,
    calorieNeeds:Double,
    token: String,
    id: String,
    context: Context,
    activity: ComponentActivity,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    ),
) {
    var calsConsumed = 0
    list.forEach { calsConsumed += it.total }
    var calsRemaining: Int

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
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

            items(list) { data ->

                var showDialog by remember { mutableStateOf(false) }
                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text("Are you sure you want to delete this?") },
                        text = { Text("This action cannot be undone") },
                        confirmButton = {
                            TextButton(onClick = {
                                showDialog = false
                                viewModel.deleteHistory(token,data.historyId)
                                Toasty.success(context,"success", Toast.LENGTH_SHORT).show()
                                viewModel.getHistory(token,id,data.date)
                            }
                            ) {
                                Text("Delete it".uppercase())
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancel".uppercase())
                            }
                        },
                    )
                }

                HistoryItem(
                    food = data.nameFood,
                    time = data.eatTime,
                    date = data.date,
                    cals = data.calorie,
                    portion = data.portion,
                    total = data.total,
                    createdAt = data.timeStamp,
                    modifier = Modifier.clickable {  showDialog = showDialog.not() }
                )
            }
        }
    }
}
