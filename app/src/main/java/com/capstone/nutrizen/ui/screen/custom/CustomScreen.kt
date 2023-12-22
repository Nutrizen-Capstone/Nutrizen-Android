package com.capstone.nutrizen.ui.screen.custom

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.BrunchDining
import androidx.compose.material.icons.filled.KebabDining
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.nutrizen.R
import com.capstone.nutrizen.activity.main.MainActivity
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.ui.components.Loading
import es.dmoral.toasty.Toasty
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomScreen(
    context: Context,
    activity: ComponentActivity,
    modifier: Modifier = Modifier,
    viewModel: CustomViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    )
) {
    var token = ""
    var id = ""
    viewModel.getSession().observeAsState().value?.let { its ->
        token = its.token
        id = its.id
    }

    viewModel.addResponse.observeAsState().value?.let {
        if (it.error) {
            Toasty.error(context, "failed, " + it.message, Toast.LENGTH_SHORT).show()
        } else {
            Toasty.success(context, it.message, Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, MainActivity::class.java))
            activity.finish()
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(title = {
            Text(
                text = stringResource(R.string.title_custom),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        })
        var loading by remember { mutableStateOf(false) }
        viewModel.isLoading.observeAsState().value?.let {
            loading = it
        }
        if (loading == true) {
            Loading(modifier = Modifier)
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Icon(
                imageVector = Icons.Outlined.Dining,
                contentDescription = "icon",
                modifier = modifier
                    .height(200.dp)
                    .width(200.dp)
                    .padding(vertical = 10.dp),
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = stringResource(id = R.string.custom_description),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))

            // #food form
            var food = remember { mutableStateOf(TextFieldValue()) }
            Row(
                modifier = modifier, verticalAlignment = Alignment.CenterVertically
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
                        imageVector = Icons.Default.BrunchDining,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_food),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(),
                    value = food.value,
                    onValueChange = { food.value = it },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text, imeAction = ImeAction.Next
                    ),
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #calorie form
            var calorie = remember { mutableStateOf(TextFieldValue()) }
            Row(
                modifier = modifier, verticalAlignment = Alignment.CenterVertically
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
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_calorie),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(),
                    value = calorie.value,
                    onValueChange = { calorie.value = it },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #portion form
            var portion = remember { mutableStateOf(TextFieldValue()) }
            Row(
                modifier = modifier, verticalAlignment = Alignment.CenterVertically
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
                        imageVector = Icons.Default.KebabDining,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.width(160.dp),
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_potion),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(),
                    value = portion.value,
                    onValueChange = { portion.value = it },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                    ),
                )
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(70.dp),

                    ) {
                    Text(
                        text = "Portion",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #eat Time dropdown menu
            val timelist = arrayOf("Select", "Breakfast", "Lunch", "Dinner", "Snack/ other")
            var expandedTime by remember { mutableStateOf(false) }
            var selectedTime by remember { mutableStateOf(timelist[0]) }
            var eatTime = selectedTime

            Row(
                modifier = modifier, verticalAlignment = Alignment.CenterVertically
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
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                ExposedDropdownMenuBox(modifier = Modifier.fillMaxWidth(),
                    expanded = expandedTime,
                    onExpandedChange = {
                        expandedTime = !expandedTime
                    }) {
                    OutlinedTextField(
                        value = selectedTime,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTime) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        label = {
                            Text(
                                text = stringResource(id = R.string.form_eatTime),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(expanded = expandedTime,
                        onDismissRequest = { expandedTime = false }) {
                        timelist.forEach { item ->
                            DropdownMenuItem(text = { Text(text = item) }, onClick = {
                                selectedTime = item
                                expandedTime = false
                            })
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            var iscomplete = false
            if (!food.value.text.trim().isEmpty() && !calorie.value.text.isEmpty() && !portion.value.text.isEmpty() && selectedTime != "Select"
            ) {
                iscomplete = true
            }
            Button(
                onClick = {
                    if (!food.value.text.trim().isEmpty() ){
                        try {
                            var total =
                                round(portion.value.text.toDouble() * calorie.value.text.toInt())
                            viewModel.addHistory(
                                token,
                                id,
                                food.value.text,
                                selectedTime,
                                calorie.value.text.toInt(),
                                portion.value.text.toDouble(),
                                total.toInt()
                            )
                        } catch (e: Exception) {
                            Toasty.error(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toasty.warning(
                            context,
                            "Failed, fill it carefully",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }, modifier = Modifier.width(300.dp), enabled = iscomplete
            ) {
                Text(text = stringResource(id = R.string.btn_toAdd))
            }
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(id = R.string.custom_alert),
                fontSize = 15.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Start
            )
        }
    }
}
