package com.capstone.nutrizen.activity.dataresult

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.SportsMartialArts
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.nutrizen.R
import com.capstone.nutrizen.activity.dataform.FormActivity
import com.capstone.nutrizen.activity.dataform.FormPage
import com.capstone.nutrizen.activity.dataform.FormViewModel
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.ui.theme.NutrizenTheme

class MyDataActivity : ComponentActivity() {
    private val viewModel by viewModels<FormViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NutrizenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyDataPage()

                }
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyDataPage(
    modifier: Modifier = Modifier,
    viewModel: FormViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    )
) {
    val context = LocalContext.current
    val activity = (LocalLifecycleOwner.current as ComponentActivity)

    var birthDate=""
    var age = 0
    var genderId = 0
    var height = 0.0
    var weight = 0.0
    var activityId = 0
    var goalId = 0
    viewModel.getSession().observeAsState().value?.let { its ->
        birthDate = its.birthDate
        age = its.age
        genderId = its.gender
        height = its.height
        weight = its.weight
        activityId = its.activity
        goalId = its.goal
    }
    var activitys: String =
        when (activityId) {
            1 -> "Little or no exercise"
            2 ->  "Light sports 3-5 days/week"
            3 ->"Moderate sports 3-5 days/week"
            4 -> "Hard sports 6-7 days/week"
            5 ->   "Hard sports 6-7 days/week + physical job"
            else -> "empty"
        }
    var gender: String=
        when (genderId) {
            1 -> "Male"
            2 -> "Female"
            else -> "empty"
        }
    var goal: String =
        when (goalId) {
            1 -> "Lose Weight"
            2 -> "Maintain Weight"
            3 -> "Gain Weight"
            else -> "empty"
        }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.btn_mydata),
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
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            // #birthdate
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
                    value = birthDate ,
                    onValueChange = {},
                    trailingIcon = {},
                    label = {
                        Text(
                            text = stringResource(id = R.string.form_birathDate),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors()
                )
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
                    value = "$age years old",
                    onValueChange = {},
                    trailingIcon = {},
                    label = {
                        Text(text = stringResource(id = R.string.form_age),
                            style = MaterialTheme.typography.bodyLarge,)
                    },
                    colors = TextFieldDefaults.textFieldColors()
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #Gender dropdown menu
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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value =  gender,
                    onValueChange = {},
                    trailingIcon = {},
                    label = {
                        Text(text = stringResource(id = R.string.form_gender),
                            style = MaterialTheme.typography.bodyLarge,)
                    },
                    colors = TextFieldDefaults.textFieldColors()
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #height form
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
                    readOnly = true,
                    value =  height.toString(),
                    onValueChange = {},
                    trailingIcon = {},
                    label = {
                        Text(text = stringResource(id = R.string.form_height),
                            style = MaterialTheme.typography.bodyLarge,)
                    },
                    colors = TextFieldDefaults.textFieldColors()
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
                    readOnly = true,
                    value =  weight.toString(),
                    onValueChange = {},
                    trailingIcon = {},
                    label = {
                        Text(text = stringResource(id = R.string.form_weight),
                            style = MaterialTheme.typography.bodyLarge,)
                    },
                    colors = TextFieldDefaults.textFieldColors()
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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value =  activitys,
                    onValueChange = {},
                    trailingIcon = {},
                    label = {
                        Text(text = stringResource(id = R.string.form_activity),
                            style = MaterialTheme.typography.bodyLarge,)
                    },
                    colors = TextFieldDefaults.textFieldColors()
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            // #Goal dropdown menu
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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    value =  goal,
                    onValueChange = {},
                    trailingIcon = {},
                    label = {
                        Text(text = stringResource(id = R.string.form_goal),
                            style = MaterialTheme.typography.bodyLarge,)
                    },
                    colors = TextFieldDefaults.textFieldColors()
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .height(50.dp)
                    .width(350.dp)
                    .clickable(onClick = {
                        context.startActivity(Intent(context, FormActivity::class.java))
                    })
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Icon(
                    imageVector = Icons.Default.Update,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 5.dp),
                    tint = Color.White
                )
                Text(
                    text = stringResource(id = R.string.reassign),
                    fontSize = 18.sp,
                    modifier = Modifier,
                    color = Color.White
                )
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

