package com.capstone.nutrizen.ui.screen.profile

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.capstone.nutrizen.R
import com.capstone.nutrizen.activity.dataresult.MyDataActivity
import com.capstone.nutrizen.activity.login.LoginActivity
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.ui.theme.NutrizenTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    ),
    context: Context,
    activity: ComponentActivity
) {
    var name: String = ""
    var email: String = ""
    var birth: String = ""
    var age: Int = 0
    var token: String = ""
    var gender: Int = 0
    var height: Double = 0.0
    var weight: Double = 0.0
    var activitys: Int = 0
    var goal: Int = 0
    viewModel.getSession().observeAsState().value?.let { its ->
        name = its.name
        email = its.email
        birth = its.birthDate
        age = its.age
        gender = its.gender
        height = its.height
        weight = its.weight
        activitys = its.activity
        goal = its.goal
        token = its.token
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.title_profile),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Icon(
                imageVector = Icons.Outlined.AccountCircle,
                contentDescription = "icon",
                modifier = modifier
                    .height(150.dp)
                    .width(150.dp),
            )
            Text(
                text = name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = MaterialTheme.colorScheme.primary,
                modifier = modifier
                    .padding(vertical = 5.dp)
            )
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                )
                Text(
                    text = email,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = modifier,
                    )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier
                    .height(50.dp)
                    .width(350.dp)
                    .clickable(onClick = {
                        context.startActivity(Intent(context, MyDataActivity::class.java))
                    })
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    tint = Color.White
                )
                Text(
                    text = stringResource(id = R.string.btn_mydata),
                    fontSize = 18.sp,
                    modifier = Modifier,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .height(50.dp)
                    .width(350.dp)
                    .clickable(onClick = {

                    })
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteForever,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    tint = Color.White
                )
                Text(
                    text = stringResource(id = R.string.btn_delete),
                    fontSize = 18.sp,
                    modifier = Modifier,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .height(50.dp)
                    .width(350.dp)
                    .clickable(onClick = {
                        viewModel.logout()
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        activity.finish()
                    })
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    tint = Color.White
                )
                Text(
                    text = stringResource(id = R.string.logout),
                    fontSize = 18.sp,
                    modifier = Modifier,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun GreetingPreview() {
    NutrizenTheme {
        ProfileScreen(
            context = LocalContext.current,
            activity = LocalLifecycleOwner.current as ComponentActivity
        )
    }
}
