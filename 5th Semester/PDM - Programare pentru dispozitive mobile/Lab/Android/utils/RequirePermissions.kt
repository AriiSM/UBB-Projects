package com.ilazar.myapp3.util

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@ExperimentalPermissionsApi
@Composable
fun RequirePermissions(
    permissions: List<String>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val permissionsState = rememberMultiplePermissionsState(permissions = permissions)
    if (permissionsState.allPermissionsGranted) {
        content()
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center // Centrează conținutul pe ecran
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Text principal
                Text(
                    text = "Acces la Cameră Necesitat",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Descriere detaliată
                Text(
                    text = getTextToShowGivenPermissions(
                        permissionsState.revokedPermissions,
                        permissionsState.shouldShowRationale
                    ),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                // Buton de solicitare permisiuni
                Button(
                    onClick = { permissionsState.launchMultiplePermissionRequest() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Permite Accesul",
                        style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissionStates: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    return if (shouldShowRationale) {
        "Aplicația are nevoie de acces la cameră pentru a captura imagini. Vă rugăm să permiteți accesul pentru a continua."
    } else {
        "Accesul la cameră este esențial pentru funcționarea aplicației. Vă rugăm să acordați permisiunile necesare."
    }
}
