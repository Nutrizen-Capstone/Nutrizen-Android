package com.capstone.nutrizen.activity.dataform

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.nutrizen.R
import com.capstone.nutrizen.activity.main.MainActivity
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.helper.getAge
import com.capstone.nutrizen.helper.toFormattedString
import com.capstone.nutrizen.helper.toMonthName
import com.capstone.nutrizen.ui.theme.NutrizenTheme
import java.util.Calendar
import java.util.Date

class FormActivity : ComponentActivity() {
    private val viewModel by viewModels<FormViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var email: String = ""
        setContent {
            NutrizenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormPage()

                    viewModel.FormResponse.observe(this) {
                        if (it.error) {
                            Toast.makeText(this, "failed, " + it.message, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    }

                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FormPage(
    modifier: Modifier = Modifier,
    viewModel: FormViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    )
) {
    val context = LocalContext.current
    val activity = (LocalLifecycleOwner.current as ComponentActivity)

    var email: String?
    viewModel.getSession().observeAsState().value.let {
        email = it?.email
    }

    var loading by remember { mutableStateOf(false) }
    viewModel.isLoading.observeAsState().value?.let {
        loading = it
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.form_title),
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

        if (loading == true) {
            Box(
                modifier = Modifier
                    .fillMaxSize().background(color = Color.Transparent),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(64.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            val keyboardController = LocalSoftwareKeyboardController.current

            Text(
                text = stringResource(id = R.string.form_description),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                fontWeight = FontWeight.Light,
                fontSize = 17.sp,
            )

            // coba password
            val username = remember { mutableStateOf(TextFieldValue()) }
            OutlinedTextField(
                modifier = modifier,
                label = { Text(text = "password") },
                value = username.value,
                onValueChange = { username.value = it },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
            )
            Spacer(modifier = Modifier.height(10.dp))

            //var date by remember { mutableStateOf(Date().time) }
            //DatePicker{date = it}

            // #birthdate
            val interactionSource = remember { MutableInteractionSource() }
            val isPressed: Boolean by interactionSource.collectIsPressedAsState()
            val currentDate = Date().toFormattedString()
            var selectedDate = remember { mutableStateOf(currentDate) }
            var age = remember { mutableStateOf(0) }
            val calendar = Calendar.getInstance()
            val year: Int = calendar.get(Calendar.YEAR)
            val month: Int = calendar.get(Calendar.MONTH)
            val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
            calendar.time = Date()
            val datePickerDialog =
                DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, month, dayOfMonth)
                    age.value = getAge(year, month, dayOfMonth)
                    selectedDate.value = "${month.toMonthName()} $dayOfMonth, $year"
                }, year, month, day)
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
                        imageVector = Icons.Default.CalendarMonth,
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
                            text = stringResource(id = R.string.form_birathDate),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors()
                )
            }
            if (isPressed) {
                datePickerDialog.show()
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #age form
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier =
                    Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.NaturePeople,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value = age.value.toString() + " years old",
                    onValueChange = {},
                    trailingIcon = {
                        /* Icon(
                         imageVector = Icons.Filled.Info,
                         contentDescription = "Error",
                         tint = MaterialTheme.colorScheme.error
                     )*/
                    },
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_age),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors()

                    /*TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray,
                    unfocusedContainerColor = Color.LightGray,
                    disabledContainerColor = Color.LightGray,
                )*/
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #Gender dropdown menu
            val genderlist = arrayOf("Select", "Male", "Female")
            var expandedGender by remember { mutableStateOf(false) }
            var selectedGender by remember { mutableStateOf(genderlist[0]) }
            var genderId: Int =
                when (selectedGender) {
                    "Male" -> 1
                    "Female" -> 2
                    else -> 0
                }

            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier =
                    Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SupervisorAccount,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                ExposedDropdownMenuBox(
                    expanded = expandedGender,
                    onExpandedChange = {
                        expandedGender = !expandedGender
                    }
                ) {
                    OutlinedTextField(
                        value = selectedGender,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGender) },
                        modifier = Modifier.menuAnchor(),
                        label = {
                            Text(
                                text = stringResource(id = R.string.form_gender),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGender,
                        onDismissRequest = { expandedGender = false }
                    ) {
                        genderlist.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedGender = item
                                    expandedGender = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #height form
            val height = remember { mutableStateOf(TextFieldValue()) }
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier =
                    Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Height,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.width(150.dp),
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_height),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(),
                    value = height.value,
                    onValueChange = { height.value = it },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                )
                Box(
                    modifier =
                    Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(55.dp),

                    ) {
                    Text(
                        text = "cm",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #weight form
            val weight = remember { mutableStateOf(TextFieldValue()) }
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier =
                    Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MonitorWeight,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                OutlinedTextField(
                    modifier = Modifier.width(150.dp),
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_weight),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(),
                    value = weight.value,
                    onValueChange = { weight.value = it },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                )
                Box(
                    modifier =
                    Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(55.dp),

                    ) {
                    Text(
                        text = "Kg",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #Activity dropdown menu
            val activitylist = arrayOf(
                "Select",
                "Little or no exercise",
                "Light sports 3-5 days/week",
                "Moderate sports 3-5 days/week",
                "Hard sports 6-7 days/week",
                "Hard sports 6-7 days/week + physical job"
            )
            var expandedActivity by remember { mutableStateOf(false) }
            var selectedActivity by remember { mutableStateOf(activitylist[0]) }
            var activityId: Int =
                when (selectedActivity) {
                    "Little or no exercise" -> 1
                    "Light sports 3-5 days/week" -> 2
                    "Moderate sports 3-5 days/week" -> 3
                    "Hard sports 6-7 days/week" -> 4
                    "Hard sports 6-7 days/week + physical job" -> 5
                    else -> 0
                }

            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier =
                    Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SportsMartialArts,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                ExposedDropdownMenuBox(
                    expanded = expandedActivity,
                    onExpandedChange = {
                        expandedActivity = !expandedActivity
                    }
                ) {
                    OutlinedTextField(
                        value = selectedActivity,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedActivity) },
                        modifier = Modifier.menuAnchor(),
                        label = {
                            Text(
                                text = stringResource(id = R.string.form_activity),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedActivity,
                        onDismissRequest = { expandedActivity = false }
                    ) {
                        activitylist.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedActivity = item
                                    expandedActivity = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #Goal dropdown menu
            val goallist = arrayOf("Select", "Lose Weight", "Stable Weight", "Gain Weight")
            var expandedGoal by remember { mutableStateOf(false) }
            var selectedGoal by remember { mutableStateOf(goallist[0]) }
            var goalId: Int =
                when (selectedGoal) {
                    "Lose Weight" -> 1
                    "Stable Weight" -> 2
                    "Gain Weight" -> 3
                    else -> 0
                }

            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier =
                    Modifier
                        .padding(horizontal = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.small
                        )
                        .height(55.dp)
                        .width(55.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MilitaryTech,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(7.dp),
                    )
                }
                ExposedDropdownMenuBox(
                    expanded = expandedGoal,
                    onExpandedChange = {
                        expandedGoal = !expandedGoal
                    }
                ) {
                    OutlinedTextField(
                        value = selectedGoal,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGoal) },
                        modifier = Modifier.menuAnchor(),
                        label = {
                            Text(
                                text = stringResource(id = R.string.form_goal),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedGoal,
                        onDismissRequest = { expandedGoal = false }
                    ) {
                        goallist.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(text = item) },
                                onClick = {
                                    selectedGoal = item
                                    expandedGoal = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            /* var coffee : Int? = 0
             dropdown(id = {
                 value: Int ->  coffee=value
             })
             */

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        try {
                            viewModel.save(
                                email.toString(),
                                username.value.text, //password
                                selectedDate.value,
                                age.value,
                                genderId,
                                height.value.text.toDouble(),
                                weight.value.text.toDouble(),
                                activityId,
                                goalId
                            )
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(55.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                ) {
                    Text(text = "Save")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormPreview() {
    NutrizenTheme {
        FormPage()
    }
}

