package com.example.smartnote.todo.ui.items

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartnote.R
import com.example.smartnote.todo.data.Note
import com.example.smartnote.utils.checkAndRequestNotificationPermission
import com.example.smartnote.utils.showSimpleNotificationWithTapAction
import com.example.smartnote.utils.ConnectivityManagerNetworkMonitor
import com.example.smartnote.utils.animation.AnimatedItemCard
import com.example.smartnote.utils.animation.LoadingRow
import com.example.smartnote.utils.camera.DeviceSensor
import com.example.smartnote.utils.camera.DisplayTemperature

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(onItemClick: (id: String?) -> Unit, onAddItem: () -> Unit, onLogout: () -> Unit) {
    Log.d("ItemsScreen", "recompose")
    val itemsViewModel = viewModel<ItemsViewModel>(factory = ItemsViewModel.Factory)
    val itemsUiState by itemsViewModel.uiState.collectAsStateWithLifecycle(initialValue = listOf())
    Log.d("ItemsScreen", "UI State: $itemsUiState")

    // Obține contextul local
    val context = LocalContext.current

    // Verificăm permisiunea înainte de a trimite notificarea
    checkAndRequestNotificationPermission(context)

    // Folosim ConnectivityManagerNetworkMonitor pentru a obține starea rețelei
    val connectivityManagerNetworkMonitor = ConnectivityManagerNetworkMonitor(context)
    val isOnline by connectivityManagerNetworkMonitor.isOnline.collectAsState(initial = true)

    // Preluăm temperatura
    val temperatureState = DeviceSensor()

    // Reîncarcăm datele după ce s-a stabilit conexiunea la internet
    LaunchedEffect(isOnline) {
        if (isOnline) {
            itemsViewModel.syncUnsyncedNotes() // Apelul funcției suspendate
        }
    }

    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Notite") },
                    actions = {
                        // Adăugăm iconița de internet în funcție de starea de conectivitate
                        Icon(
                            painter = painterResource(
                                id = if (isOnline) {
                                    R.drawable.ic_internet_connected
                                } else R.drawable.ic_no_internet
                            ),
                            contentDescription = "Network Status",
                            tint = if (isOnline) Color.Green else Color.Red,
                            modifier = Modifier.padding(8.dp)
                        )

                        DisplayTemperature(temperatureState)

                        Button(onClick = onLogout) { Text("Logout") }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        Log.d("ItemsScreen", "Floating Action Button Clicked")
                        onAddItem()
                    },
                ) { Icon(Icons.Rounded.Add, contentDescription = "Add Item") }
            }
        ) {
            ItemList(
                itemList = itemsUiState,
                onItemClick = { id ->
                    val selectedItem = itemsUiState.find { it.id == id }
                    selectedItem?.let {
                        // Afișăm notificarea care redirecționează la item
                        showSimpleNotificationWithTapAction(
                            context = context,
                            channelId = "MyTestChannel",
                            notificationId = selectedItem.id.toInt(),
                            textTitle = selectedItem.titlu,
                            textContent = selectedItem.descriere,
                            noteId = selectedItem.id // Folosim hashCode ca un id pentru item
                        )
                    }
                    onItemClick(id)
                },
                modifier = Modifier.padding(it)
            )
        }
    }
}

@Composable
fun ItemList(
    itemList: List<Note>,
    onItemClick: (id: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (itemList.isEmpty()) {
        LoadingRow() // Afișăm animația de loading
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            items(itemList, key = { note -> note.id }) { note -> // Utilizare key pentru optimizarea performanței
                AnimatedItemCard(note = note, onClick = {
                    onItemClick(note.id)
                }, index = itemList.indexOf(note)) // Adăugăm indexul pentru animație progresivă
            }
        }
    }
}



@Composable
fun ItemCard(note: Note, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.80f),
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Row pentru titlu și pictogramă
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Title: ${note.titlu}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                // Verificăm dacă există imagine și adăugăm pictograma
                if (!note.imageUri.isNullOrEmpty()) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_imagine), // Folosim o pictogramă de imagine
                        contentDescription = "Image Icon",
                        modifier = Modifier
                            .padding(start = 8.dp) // Distanță față de titlu
                            .size(20.dp), // Dimensiune mică pentru icon
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = "Priority: ${note.prioritate}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun PreviewItemsScreen() {
    ItemsScreen(onItemClick = {}, onAddItem = {}, onLogout = {})
}
