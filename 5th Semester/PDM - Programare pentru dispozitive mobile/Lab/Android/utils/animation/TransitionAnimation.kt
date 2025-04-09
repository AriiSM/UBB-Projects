package com.example.smartnote.utils.animation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartnote.R
import com.example.smartnote.todo.data.Note

// Animația pentru cardurile de iteme
@Composable
fun AnimatedItemCard(note: Note, onClick: () -> Unit, index: Int) {
    var isVisible by remember { mutableStateOf(false) }

    // Întârziere progresivă pentru animarea fiecărui item
    LaunchedEffect(note.id) {
        kotlinx.coroutines.delay((300 * index).toLong()) // Întârziere progresivă
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(durationMillis = 1200)) + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut(animationSpec = tween(durationMillis = 600)) + slideOutVertically(targetOffsetY = { -it })
    ) {
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Title: ${note.titlu}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    // Dacă există imagine, adăugăm pictograma
                    if (!note.imageUri.isNullOrEmpty()) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_imagine), // Pictogramă pentru imagine
                            contentDescription = "Image Icon",
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .size(20.dp),
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
}


// Animația de încărcare
@Composable
fun LoadingRow() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000 // Creștem durata animației la 2000ms
                0.7f at 1000 // Schimbăm valoarea intermediară
            },
            repeatMode = RepeatMode.Reverse
        )
    )

    Row(
        modifier = Modifier
            .heightIn(min = 64.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = alpha))
        )
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(Color.LightGray.copy(alpha = alpha))
        )
    }
}
