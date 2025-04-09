package com.example.smartnote.todo.ui.item

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.smartnote.R
import com.example.smartnote.utils.ConnectivityManagerNetworkMonitor
import com.example.smartnote.utils.showSimpleNotification
import com.example.smartnote.utils.camera.MyCamera
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ilazar.mycamera.gallery.GallerySelect
import java.io.File
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import coil.compose.AsyncImage

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NewItemScreen(
    itemId: String,
    onClose: () -> Unit,
    onAddItem: (String, String, String, String, Boolean, String?) -> Unit
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    val connectivityManagerNetworkMonitor = ConnectivityManagerNetworkMonitor(context)
    val isOnline by connectivityManagerNetworkMonitor.isOnline.collectAsState(initial = true)

    var titlu by remember { mutableStateOf("") }
    var descriere by remember { mutableStateOf("") }
    var data by remember { mutableStateOf("") }
    var prioritate by remember { mutableStateOf(1) }
    var complet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showCamera by remember { mutableStateOf(false) }
    var showGallery by remember { mutableStateOf(false) }

    MaterialTheme(colorScheme = darkColorScheme()) {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text(text = stringResource(R.string.item)) })
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                color = MaterialTheme.colorScheme.background
            ) {
                if (showCamera) {
                    MyCamera(
                        modifier = Modifier.fillMaxSize(),
                        onImageCaptured = { file ->
                            imageUri = Uri.fromFile(file)
                            showCamera = false
                        }
                    )
                } else if (showGallery) {
                    GallerySelect(
                        modifier = Modifier.fillMaxSize(),
                        onImageUri = { uri ->
                            imageUri = uri
                            showGallery = false
                        }
                    )
                } else {
                    val scrollState = rememberScrollState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(16.dp)
                    ) {
                        TextField(
                            value = titlu,
                            onValueChange = { titlu = it },
                            label = { Text("Titlu") }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(
                            value = descriere,
                            onValueChange = { descriere = it },
                            label = { Text("Descriere") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            maxLines = 10,
                            singleLine = false
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Data selectată: $data")
                        Button(onClick = { showDatePicker = true }) {
                            Text("Selectează data")
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        if (showDatePicker) {
                            DatePickerDialog(
                                onDismissRequest = { showDatePicker = false },
                                confirmButton = {
                                    TextButton(onClick = {
                                        datePickerState.selectedDateMillis?.let {
                                            val selectedDate = Instant.ofEpochMilli(it)
                                                .atZone(ZoneId.systemDefault())
                                                .withHour(11)
                                                .toInstant()
                                            val formatter = DateTimeFormatter.ISO_INSTANT
                                            data = formatter.format(selectedDate)
                                        }
                                        showDatePicker = false
                                    }) {
                                        Text("Confirmă")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDatePicker = false }) {
                                        Text("Anulează")
                                    }
                                }
                            ) {
                                DatePicker(state = datePickerState)
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        var expanded by remember { mutableStateOf(false) }
                        val priorityOptions = (1..10).map { it.toString() }
                        var selectedPriority by remember { mutableStateOf(prioritate.toString()) }

                        Text("Selectează prioritatea:")
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            TextField(
                                value = selectedPriority,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Prioritate") },
                                modifier = Modifier.menuAnchor()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                priorityOptions.forEach { priority ->
                                    DropdownMenuItem(
                                        text = { Text(priority) },
                                        onClick = {
                                            selectedPriority = priority
                                            prioritate = priority.toInt()
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (imageUri == null) {
                                        showCamera = true
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Take Photo")
                            }

                            Button(
                                onClick = {
                                    if (imageUri == null) {
                                        showGallery = true
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Select Photo")
                            }
                        }

                        imageUri?.let { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = "Imagine selectată",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        if (imageUri != null) {
                            Button(
                                onClick = { imageUri = null },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Remove Image")
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(onClick = {
                            onAddItem(titlu, descriere, data, prioritate.toString(), complet, imageUri?.toString())
                            navController.popBackStack()

                            val notificationId = UUID.randomUUID().hashCode()
                            if (isOnline) {
                                showSimpleNotification(
                                    context = context,
                                    channelId = "MyTestChannel",
                                    notificationId = notificationId,
                                    textTitle = "SERVER_Titlu: $titlu",
                                    textContent = "Ai noroc, s-a salvat cu succes pe server"
                                )
                            } else {
                                showSimpleNotification(
                                    context = context,
                                    channelId = "MyTestChannel",
                                    notificationId = notificationId,
                                    textTitle = "LOCAL_Titlu: $titlu",
                                    textContent = "Dupa reconectarea la internet se v-a salva pe server"
                                )
                            }
                        }) {
                            Text("Adaugă")
                        }
                    }
                }
            }
        }
    }
}
