package com.capstone.nutrizen.activity.login

import android.content.Intent
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.capstone.nutrizen.activity.dataform.FormActivity
import com.capstone.nutrizen.activity.main.MainActivity
import com.capstone.nutrizen.activity.register.RegisterActivity
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.ui.theme.NutrizenTheme

class LoginActivity : ComponentActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getSession().observe(this) { user ->
            if (user.isLogin && user.isDataCompleted) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else if (user.isLogin == true && user.isDataCompleted == false) {
                startActivity(Intent(this, FormActivity::class.java))
                finish()
            } else {
                setContent {
                    NutrizenTheme {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            LoginPage()
                        }
                    }
                }
            }
        }

        viewModel.loginResponse.observe(this) {
            if (it.error) {
                Toast.makeText(this, "failed, " + it.loginResult, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    )
) {
    val mContext = LocalContext.current


    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val username = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }
            var errorpassword by remember { mutableStateOf(false) }
            var errorusername by remember { mutableStateOf(false) }
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
                text = "Login",
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
                } ,trailingIcon = {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null)
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
                    imeAction = ImeAction.Default
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
            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = {
                        errorpassword = password.value.text.isEmpty()
                        errorusername = username.value.text.isEmpty()
                        if (errorpassword == false && errorusername == false) {
                            viewModel.login(username.value.text, password.value.text)
                        } else
                            Toast.makeText(
                                mContext,
                                "Failed, fill it carefully",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "Login")
                }
            }
            Spacer(modifier = Modifier.height(30.dp))

            Row(modifier = Modifier, horizontalArrangement = Arrangement.Center) {
                Text(text = "Don't have an account? ")
                ClickableText(
                    text = AnnotatedString("Sign up"),
                    modifier = Modifier.padding(horizontal = 10.dp),
                    onClick = {
                        mContext.startActivity(
                            Intent(
                                mContext,
                                RegisterActivity::class.java
                            )
                        )
                    },
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

@Composable
fun coba(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
