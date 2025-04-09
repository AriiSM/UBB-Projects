package com.ilazar.mycamera.gallery

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ilazar.myapp3.util.RequirePermissions

@ExperimentalPermissionsApi
@Composable
fun GallerySelect(
    modifier: Modifier,
    onImageUri: (Uri) -> Unit = { }
) {
    // Launcher pentru a obține o imagine din galeria de imagini
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            onImageUri(uri ?: EMPTY_IMAGE_URI)  // Dacă nu există nici o imagine selectată, se folosește o valoare goală
        }
    )

    // Funcție pentru a lansa galeria
    @Composable
    fun launchGallery() {
        SideEffect {
            launcher.launch("image/*") // Lansează galeria pentru a selecta o imagine
        }
    }

    // Verificarea permisiunilor pentru Android 10+ (API 29) și versiuni mai recente
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        RequirePermissions(
            permissions = listOf(Manifest.permission.ACCESS_MEDIA_LOCATION),
            modifier = modifier
        ) {
            launchGallery() // Lansează galeria doar dacă permisiunile sunt acordate
        }
    } else {
        launchGallery() // Pe versiuni mai vechi de Android, nu sunt necesare permisiuni suplimentare
    }
}

// Definirea unei valori implicite pentru o imagine goală
val EMPTY_IMAGE_URI: Uri = Uri.parse("file://dev/null")
