package com.capstone.nutrizen.activity.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.nutrizen.R
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.ui.theme.NutrizenTheme

class RegisterActivity : ComponentActivity() {
    private val viewModel by viewModels<RegisterViewModel> {
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
                    RegisterPage()
                }
            }
        }
        viewModel.signupResponse.observe(this) {
            if (it.error==true) {
                Toast.makeText(this, "failed, "+it.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterPage(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    )
) {
    val mContext = LocalContext.current
    val activity = (LocalLifecycleOwner.current as ComponentActivity)

    Box(modifier = Modifier.fillMaxSize()) {

        ClickableText(
            text = AnnotatedString("Login here"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = { activity.finish() },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default,
                textDecoration = TextDecoration.Underline,
                color = colorResource(id = R.color.purple_500)
            )
        )
    }
    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val password2 = remember { mutableStateOf(TextFieldValue()) }
        var errorpassword by remember { mutableStateOf(false) }
        var errorpassword2 by remember { mutableStateOf(false) }
        var errorusername by remember{ mutableStateOf(false) }
        var isPasswordVisible by remember { mutableStateOf(false) }
        val keyboardController = LocalSoftwareKeyboardController.current

        Text(
            text = "Register",
            style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Cursive)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = modifier,
            label = { Text(text = "Username") },
            value = username.value,
            onValueChange = { username.value = it },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = errorusername,
            supportingText = {
                if (errorusername) {
                    Text(text = "Please enter your username")
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = modifier,
            value = password.value,
            onValueChange = { password.value = it },
            label = {
                Text(text = "password")
            },
            trailingIcon = {
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {

                    val visibleIconAndText = Pair(
                        first = Icons.Outlined.Visibility,
                        second = stringResource(id = R.string.lorem_ipsum)
                    )

                    val hiddenIconAndText = Pair(
                        first = Icons.Outlined.VisibilityOff,
                        second = stringResource(id = R.string.previous)
                    )

                    val passwordVisibilityIconAndText =
                        if (isPasswordVisible) visibleIconAndText else hiddenIconAndText

                    // Render Icon
                    Icon(
                        imageVector = passwordVisibilityIconAndText.first,
                        contentDescription = passwordVisibilityIconAndText.second
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            isError = errorpassword,
            supportingText = {
                if (errorpassword) {
                    Text(text = "Please enter your password")
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            modifier = modifier,
            value = password2.value,
            onValueChange = { password2.value = it },
            label = {
                Text(text = "confirm password")
            },
            trailingIcon = {
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {

                    val visibleIconAndText = Pair(
                        first = Icons.Outlined.Visibility,
                        second = stringResource(id = R.string.lorem_ipsum)
                    )

                    val hiddenIconAndText = Pair(
                        first = Icons.Outlined.VisibilityOff,
                        second = stringResource(id = R.string.previous)
                    )

                    val passwordVisibilityIconAndText =
                        if (isPasswordVisible) visibleIconAndText else hiddenIconAndText

                    // Render Icon
                    Icon(
                        imageVector = passwordVisibilityIconAndText.first,
                        contentDescription = passwordVisibilityIconAndText.second
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Default
            ),
            keyboardActions = KeyboardActions(onDone = {
                keyboardController?.hide()
            }),
            isError = errorpassword2,
            supportingText = {
                if (errorpassword2) {
                    Text(text = "Please enter your confirm password")
                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    errorpassword = password.value.text.isEmpty()
                    errorusername = username.value.text.isEmpty()
                    errorpassword2 = password2.value.text.isEmpty()
                    if (errorpassword == false && errorpassword2 == false && errorusername == false) {
                        viewModel.register(
                            password2.value.text,
                            username.value.text,
                            password.value.text
                        )
                    } else
                        Toast.makeText(mContext, "Failed, fill it carefully ${username}", Toast.LENGTH_SHORT)
                            .show()
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Register")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

    }
}
