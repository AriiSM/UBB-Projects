package com.example.smartnote.todo.ui.item


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.smartnote.R
import java.text.SimpleDateFormat


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemScreen(itemId: String, onClose: () -> Unit) {
    val itemViewModel = viewModel<ItemViewModel>(factory = ItemViewModel.Factory(itemId))
    val itemUiState = itemViewModel.uiState
    val item = itemUiState.item

    MaterialTheme(
        colorScheme = darkColorScheme()
    ) {
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
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        item?.let {
                            // Format and display the date
                            val formattedDate = try {
                                val inputFormat = SimpleDateFormat(
                                    "yyyy-MM-dd'T'HH:mm:ss'Z'",
                                    java.util.Locale("en", "US")
                                )
                                val outputFormat =
                                    SimpleDateFormat("dd/MM/yyyy", java.util.Locale("en", "US"))

                                val date = inputFormat.parse(it.data)
                                outputFormat.format(date!!)
                            } catch (e: Exception) {
                                "Invalid Date"
                            }

                            Text(
                                text = it.titlu,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = it.descriere,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Data: $formattedDate",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Prioritate: ${it.prioritate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = priorityColor(it.prioritate)
                            )

                            // Display the image if available
                            it.imageUri?.let { uri ->
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Item Image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp) // Adjust the size as needed
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (it.complet) "Complet" else "Incomplet",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (it.complet) Color.Green else Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

fun priorityColor(priority: Int): Color {
    return when {
        priority <= 2 -> Color.Green // Prioritate mică (1-2)
        priority <= 5 -> Color.Yellow // Prioritate medie (3-5)
        priority <= 8 -> Color.Magenta // Prioritate mare (6-8)
        else -> Color.Red // Prioritate critică (9-10)
    }
}



@Preview
@Composable
fun PreviewItemScreen() {
    ItemScreen(itemId = "0", onClose = {})
}

