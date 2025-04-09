package com.example.smartnote.utils.camera

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.myapp3.camera.CameraCapture
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.ilazar.myapp3.util.RequirePermissions
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MyCamera(
    modifier: Modifier = Modifier,
    onImageCaptured: (File) -> Unit // Adăugăm parametrul callback
) {
    RequirePermissions(
        permissions = listOf(
            Manifest.permission.CAMERA,
        ),
        modifier = modifier
    ) {
        CameraCapture(
            modifier = modifier,
            onImageFile = onImageCaptured // Transmitem callback-ul către CameraCapture
        )
    }
}
