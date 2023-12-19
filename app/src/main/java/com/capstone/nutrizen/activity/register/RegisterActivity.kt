package com.capstone.nutrizen.activity.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.nutrizen.R
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.ui.components.Loading
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
            if (it.error == true) {
                Toast.makeText(this, "failed, " + it.message, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    )
) {
    val mContext = LocalContext.current
    val activity = (LocalLifecycleOwner.current as ComponentActivity)

    var loading by remember { mutableStateOf(false) }
    viewModel.isLoading.observeAsState().value?.let {
        loading = it
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        if (loading == true) {
            Loading(modifier = Modifier)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val username = remember { mutableStateOf(TextFieldValue()) }
            val name = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }
            val password2 = remember { mutableStateOf(TextFieldValue()) }
            var errorpassword by remember { mutableStateOf(false) }
            var errorpassword2 by remember { mutableStateOf(false) }
            var errorusername by remember { mutableStateOf(false) }
            var errorname by remember { mutableStateOf(false) }
            var isPasswordVisible by remember { mutableStateOf(false) }
            val keyboardController = LocalSoftwareKeyboardController.current

            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_nutrizen),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clip(CircleShape),
                    contentDescription = "logo"
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = TextStyle(fontSize = 50.sp, fontFamily = FontFamily.Serif)
                )
            }
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
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
                }
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = modifier,
                label = { Text(text = "Name") },
                value = name.value,
                onValueChange = { name.value = it },
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = errorname,
                supportingText = {
                    if (errorname) {
                        Text(text = "Please enter your name")
                    }
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                }
            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = modifier,
                value = password.value,
                onValueChange = { password.value = it },
                label = {
                    Text(text = "Password")
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
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(
                modifier = modifier,
                value = password2.value,
                onValueChange = { password2.value = it },
                label = {
                    Text(text = "Confirm Password")
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
            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        errorpassword = password.value.text.isEmpty()
                        errorusername = username.value.text.isEmpty()
                        errorpassword2 = password2.value.text.isEmpty()
                        errorname = username.value.text.isEmpty()
                        if (errorpassword == false && errorpassword2 == false && errorusername == false&& errorname==false) {
                            viewModel.register(
                                name.value.text,
                                username.value.text,
                                password.value.text,
                                password2.value.text
                            )
                        } else
                            Toast.makeText(
                                mContext,
                                "Failed, fill it carefully ${username}",
                                Toast.LENGTH_SHORT
                            )
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
            Spacer(modifier = Modifier.height(30.dp))

            Row(modifier = Modifier, horizontalArrangement = Arrangement.Center) {
                Text(text = "Already have an account? ")
                ClickableText(
                    text = AnnotatedString("Login"),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = { activity.finish() },
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = FontFamily.Default,
                        color = colorResource(id = R.color.purple_700)
                    )
                )
            }

        }
    }
}
