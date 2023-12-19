package com.capstone.nutrizen.activity.scan

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.KebabDining
import androidx.compose.material.icons.outlined.AddPhotoAlternate
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.capstone.nutrizen.R
import com.capstone.nutrizen.data.Injection
import com.capstone.nutrizen.data.ViewModelFactory
import com.capstone.nutrizen.helper.TensorFLowHelper
import com.capstone.nutrizen.helper.createImageFile
import com.capstone.nutrizen.ui.components.Loading
import com.capstone.nutrizen.ui.theme.NutrizenTheme
import java.util.Objects
import kotlin.math.round


class ScanActivity : ComponentActivity() {
    private val viewModel by viewModels<ScanViewModel> {
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
                    ScanPage()

                    viewModel.addResponse.observe(this) {
                        if (it.error) {
                            Toast.makeText(this, "failed, " + it.message, Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanPage(
    modifier: Modifier = Modifier,
    viewModel: ScanViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository(context = LocalContext.current))
    )
) {
    var token = ""
    var id = ""
    viewModel.getSession().observeAsState().value?.let { its ->
        token = its.token
        id = its.id
    }

    var food = ""
    var eatTime = ""
    var portionId = ""
    var calorie = 0

    //The URI of the photo that the user has picked
    var photoUri: Uri? by remember { mutableStateOf(null) }
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    //The launcher we will use for the PickVisualMedia contract.
    //When .launch()ed, this will display the photo picker.
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            //When the user has selected a photo, its URI is returned here
            photoUri = uri
        }

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        context.getPackageName() + ".provider", file
    )
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            photoUri = uri
        }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.title_scan),
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
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            var loading by remember { mutableStateOf(false) }
            viewModel.isLoading.observeAsState().value?.let {
                loading = it
            }
            if (loading == true) {
                Loading(modifier = Modifier)
            }

            Spacer(modifier = Modifier.height(5.dp))
            photoUri?.let {
                if (Build.VERSION.SDK_INT < 28)
                    bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                else {
                    try {
                        val source = ImageDecoder.createSource(context.contentResolver, it)
                        bitmap = ImageDecoder.decodeBitmap(
                            source,
                            ImageDecoder.OnHeaderDecodedListener { decoder, info, source ->
                                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                                decoder.isMutableRequired = true
                            })
                    } catch (e: Exception) {
                        bitmap = null
                        photoUri = null
                        Toast.makeText(LocalContext.current, "${e.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                //Use Coil to display the selected image
                val painter = rememberAsyncImagePainter(
                    ImageRequest
                        .Builder(LocalContext.current)
                        .data(data = photoUri)
                        .build()
                )
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .height(300.dp)
                        .width(300.dp)
                        .border(6.dp, Color.Gray),
                    contentScale = ContentScale.Crop
                )
            }
            if (photoUri == null) {
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.large
                        )
                        .height(300.dp)
                        .width(300.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddPhotoAlternate,
                        contentDescription = "icon",
                        modifier = modifier
                            .fillMaxSize()
                            .padding(20.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        //On button press, launch the photo picker
                        launcher.launch(
                            PickVisualMediaRequest(
                                //Here we request only photos. Change this to .ImageAndVideo if you want videos too.
                                //Or use .VideoOnly if you only want videos.
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }, modifier = Modifier.width(120.dp)
                ) {
                    Text(stringResource(id = R.string.btn_gallery))
                }
                Spacer(modifier = Modifier.width(30.dp))
                Button(
                    onClick = {
                        //On button press, launch the camera
                        cameraLauncher.launch(uri)
                    }, modifier = Modifier.width(120.dp)
                ) {
                    Text(stringResource(id = R.string.btn_camera))
                }
            }

            bitmap?.let {
                val scaledBitmap = Bitmap.createScaledBitmap(
                    it,
                    TensorFLowHelper.imageSize,
                    TensorFLowHelper.imageSize, false
                );
                TensorFLowHelper.classifyImage(scaledBitmap) {
                    food = it
                    calorie =
                        when (food) {
                            "ayam bakar" -> 242
                            "ayam goreng" -> 260
                            "bakso" -> 200
                            "bakwan" -> 280
                            "batagor" -> 290
                            "bihun" -> 100
                            "bika_ambon" -> 164
                            "capcay" -> 83
                            "dadar_gulung" -> 131
                            "gado-gado" -> 137
                            "ikan goreng" -> 416
                            "kerupuk" -> 201
                            "martabak telur" -> 186
                            "mie" -> 113
                            "nasi goreng" -> 168
                            "nasi putih" -> 180
                            "nugget" -> 297
                            "opor ayam" -> 163
                            "pempek" -> 195
                            "rendang" -> 58
                            "roti" -> 248
                            "sate" -> 200
                            "sosis" -> 38
                            "soto" -> 150
                            "steak" -> 252
                            "tahu" -> 115
                            "telur" -> 62
                            "tempe" -> 336
                            "terong balado" -> 97
                            "tumis kangkung" -> 98
                            "udang" -> 144
                            else -> 0
                        }

                    Row(
                        modifier = Modifier
                            .height(70.dp)
                            .width(350.dp)
                            .padding(10.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = MaterialTheme.shapes.medium
                            ),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Fastfood,
                            contentDescription = "icon",
                            modifier = modifier
                                .height(50.dp)
                                .width(50.dp)
                                .padding(horizontal = 10.dp),
                        )
                        Text(
                            text = it,
                            modifier = Modifier
                                .padding(horizontal = 10.dp),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
            if (bitmap == null) {
                Row(
                    modifier = Modifier
                        .height(70.dp)
                        .width(350.dp)
                        .padding(10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = MaterialTheme.shapes.medium
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "icon",
                        modifier = modifier
                            .height(50.dp)
                            .width(50.dp)
                            .padding(horizontal = 10.dp),
                    )
                    Text(
                        text = "Pick Image and Add Food",
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                        fontWeight = FontWeight.Light,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Start
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {

                // #portion form
                val portion = remember { mutableStateOf("1") }
                portionId = portion.value
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
                            imageVector = Icons.Default.KebabDining,
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
                                text = stringResource(id = R.string.form_potion),
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(),
                        value = portion.value,
                        onValueChange = { portion.value = it },
                        maxLines = 1,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ), enabled = photoUri != null
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
                            .width(70.dp),

                        ) {
                        Text(
                            text = "Portion",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyLarge,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))

                // #eat Time dropdown menu
                val timelist = arrayOf("Select", "Breakfast", "Lunch", "Dinner", "Snack/ other")
                var expandedTime by remember { mutableStateOf(false) }
                var selectedTime by remember { mutableStateOf(timelist[0]) }
                eatTime = selectedTime

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
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "icon",
                            modifier = modifier
                                .fillMaxSize()
                                .padding(7.dp),
                        )
                    }
                    ExposedDropdownMenuBox(
                        expanded = expandedTime,
                        onExpandedChange = {
                            expandedTime = !expandedTime
                        }
                    ) {
                        OutlinedTextField(
                            value = selectedTime,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTime) },
                            modifier = Modifier
                                .menuAnchor()
                                .width(230.dp),
                            label = {
                                Text(
                                    text = stringResource(id = R.string.form_eatTime),
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            },
                            colors = TextFieldDefaults.textFieldColors(),
                            enabled = photoUri != null
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTime,
                            onDismissRequest = { expandedTime = false }
                        ) {
                            timelist.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        selectedTime = item
                                        expandedTime = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = {
                    if (eatTime == "Select") {
                        Toast.makeText(context, "select eating time first!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        try {
                            var total = round(portionId.toDouble() * calorie).toInt()
                            viewModel.addHistory(
                                token, id, food, eatTime, calorie, portionId.toDouble(), total
                            )
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                }, modifier = Modifier.width(300.dp), enabled = photoUri != null
            ) {
                Text(stringResource(id = R.string.btn_toAdd))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NutrizenTheme {
    }
}