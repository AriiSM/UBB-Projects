package com.example.myapp.auth

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartnote.R

val TAG = "LoginScreen"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onClose: () -> Unit) {
    val loginViewModel = viewModel<LoginViewModel>(factory = LoginViewModel.Factory)
    val loginUiState = loginViewModel.uiState

    MaterialTheme(colorScheme = darkColorScheme()) { // Aplica tema Dark
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(id = R.string.login)) }
                )
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                var username by remember { mutableStateOf("") }
                TextField(
                    label = { Text(text = "Username") },
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                var password by remember { mutableStateOf("") }
                TextField(
                    label = { Text(text = "Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Button(
                    onClick = { loginViewModel.login(username, password) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loginUiState.isAuthenticating
                ) {
                    Text(text = "Login")
                }
                if (loginUiState.isAuthenticating) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                    )
                }
                loginUiState.authenticationError?.let { error ->
                    Text(
                        text = "Login failed: ${error.message}",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }

    LaunchedEffect(loginUiState.authenticationCompleted) {
        if (loginUiState.authenticationCompleted) {
            onClose() // Close the login screen once authentication is complete
        }
    }
}



@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen({})
}