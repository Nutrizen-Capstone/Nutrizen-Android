package com.capstone.nutrizen.ui.screen.history

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.capstone.nutrizen.helper.toSimpleString
import com.capstone.nutrizen.ui.common.UiState
import com.capstone.nutrizen.ui.components.HistoryItem
import java.util.Calendar
import java.util.Date

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
    var id=""
    viewModel.getSession().observeAsState().value?.let { its ->
        token = its.token
        id = its.id
    }

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
        val currentDate = Date().toSimpleString()
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
                    selectedDate.value = "$year-${month+1}-$dayOfMonth"
                    viewModel.getHistory(token,id,selectedDate.value)
                },
                year,
                month,
                day
            )
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
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
                        imageVector = Icons.Default.EditCalendar,
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

        viewModel.getHistory(token,id, selectedDate.value)
        viewModel.uiStates.collectAsState().value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    viewModel.getHistory(token,id, selectedDate.value)
                }

                is UiState.Success -> {
                    HistoryContent(list = uiState.data, token,id,context, activity)
                }

                is UiState.Error -> {
                    val TAG = "errors"
                    Log.d(TAG, "onFAILED: ${uiState.copy()}")
                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(onClick = { viewModel.getHistory(token,id, selectedDate.value)
                            Toast.makeText(context, selectedDate.value, Toast.LENGTH_SHORT).show()}) {
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
    token:String,
    id:String,
    context: Context,
    activity: ComponentActivity,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    ),
) {
    var sum = 0
    list.forEach {
        sum += it.total
    }

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

            }
            items(list) { data ->
                HistoryItem(
                    food = data.nameFood,
                    time = data.eatTime,
                    date = data.date,
                    cals =data.calorie,
                    portion = data.portion,
                    total = data.total
                )
            }
        }
    }
}
