package com.capstone.nutrizen.activity.dataform

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
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
import java.util.*

class FormActivity : ComponentActivity() {
    private val viewModel by viewModels<FormViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var email :String = ""
        setContent {
            NutrizenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FormPage()
                    viewModel.FormResponse.observe(this){
                        if (it.error) {
                            Toast.makeText(this, "failed, "+it.message, Toast.LENGTH_SHORT).show()
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


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FormPage(
    modifier: Modifier = Modifier,
    viewModel: FormViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    )
) {
    val context = LocalContext.current
    val activity = (LocalLifecycleOwner.current as ComponentActivity)
    var email:String?=null
    viewModel.getSession().observeAsState().value.let {
        email = it?.email
    }
    Column(
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val keyboardController = LocalSoftwareKeyboardController.current
        Text(
            text = "Personal Data Form",
            style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // coba
        val username = remember { mutableStateOf(TextFieldValue()) }
        OutlinedTextField(
            modifier = modifier,
            label = { Text(text = "Username") },
            value = username.value,
            onValueChange = { username.value = it },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
        )


        //var date by remember { mutableStateOf(Date().time) }
        //DatePicker{date = it}

        // birtdate
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
        Text(
            text = email.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        val datePickerDialog =
            DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val newDate = Calendar.getInstance()
                newDate.set(year, month, dayOfMonth)
                age.value = getAge(year,month,dayOfMonth)
                selectedDate.value = "${month.toMonthName()} $dayOfMonth, $year"
            }, year, month, day)
        Row(modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "icon",
                modifier = modifier,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                value = selectedDate.value,
                onValueChange = {},
                trailingIcon = { Icons.Default.DateRange },
                interactionSource = interactionSource
            )
        }
        if (isPressed) {
            datePickerDialog.show()
        }
        Spacer(modifier = Modifier.height(20.dp))

        // age
        Text(
            text = stringResource(id = R.string.form_age),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.NaturePeople,
                contentDescription = "icon",
                modifier = modifier,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                value = age.value.toString()+" years old",
                onValueChange = {},
                trailingIcon = { Icons.Default.NaturePeople },
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {

                   viewModel.save(email.toString(),username.value.text,age.value)
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Save")
            }
        }
    }
}


