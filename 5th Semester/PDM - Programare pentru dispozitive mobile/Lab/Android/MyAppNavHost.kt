package com.example.smartnote

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.myapp.auth.LoginScreen
import com.example.smartnote.core.data.UserPreferences
import com.example.smartnote.core.data.remote.Api
import com.example.smartnote.core.ui.UserPreferencesViewModel
import com.example.smartnote.todo.ui.item.ItemScreen
import com.example.smartnote.todo.ui.items.ItemsScreen
import com.example.smartnote.todo.ui.item.NewItemScreen


val itemsRoute = "notes"
val authRoute = "auth"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyAppNavHost(
    navController: NavHostController,
    noteId: String? = null  // Adăugăm parametrul noteId
) {
    val onCloseItem = {
        Log.d("MyAppNavHost", "navigate back to list")
        navController.popBackStack()
    }

    val userPreferencesViewModel =
        viewModel<UserPreferencesViewModel>(factory = UserPreferencesViewModel.Factory)

    val userPreferencesUiState by userPreferencesViewModel.uiState.collectAsStateWithLifecycle(
        initialValue = UserPreferences()
    )

    val myAppViewModel = viewModel<MyAppViewModel>(factory = MyAppViewModel.Factory)

    NavHost(
        navController = navController,
        startDestination = authRoute
    ) {
        // Rutele pentru vizualizarea listei de note
        composable(itemsRoute) {
            ItemsScreen(
                onItemClick = { itemId ->
                    Log.d("MyAppNavHost", "navigate to item $itemId")
                    navController.navigate("$itemsRoute/$itemId")  // Navighează la detaliile notei
                },
                onAddItem = {
                    Log.d("MyAppNavHost", "navigate to new item")
                    navController.navigate("$itemsRoute-new")
                },
                onLogout = {
                    Log.d("MyAppNavHost", "logout")
                    myAppViewModel.logout()
                    Api.tokenInterceptor.token = null
                    navController.navigate(authRoute) {
                        popUpTo(0)
                    }
                })
        }

        // Rutele pentru detaliile notei
        composable(
            route = "$itemsRoute/{noteId}",
            arguments = listOf(navArgument("noteId") { type = NavType.StringType })
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId").toString()
            Log.d("MyAppNavHost", "Navigating to note with ID: $noteId")
            ItemScreen(
                itemId = noteId,
                onClose = { onCloseItem() }
            )
        }

        // Rutele pentru crearea unei noi note
        composable(route = "$itemsRoute-new") {
            NewItemScreen(
                itemId = it.arguments?.getString("id").toString(),
                onClose = { navController.popBackStack() },
                onAddItem = { titlu, descriere, data, prioritate, complet, imageUri ->
                    myAppViewModel.addItem(titlu, descriere, data, prioritate, complet, imageUri)
                    navController.popBackStack() // Navigate back to the list
                }
            )
        }


        // Rutele pentru autentificare
        composable(route = authRoute) {
            LoginScreen(
                onClose = {
                    Log.d("MyAppNavHost", "navigate to list")
                    navController.navigate(itemsRoute)
                }
            )
        }
    }

    // Dacă există un token valid, navighează automat la lista de note
    LaunchedEffect(userPreferencesUiState.token) {
        if (userPreferencesUiState.token.isNotEmpty()) {
            Log.d("MyAppNavHost", "Launched effect navigate to items")
            Api.tokenInterceptor.token = userPreferencesUiState.token
            myAppViewModel.setToken(userPreferencesUiState.token)
            navController.navigate(itemsRoute) {
                popUpTo(0)
            }
        }
    }

    // Navigarea automată către detaliile notei atunci când este prezent `noteId`
    LaunchedEffect(noteId) {
        if (!noteId.isNullOrEmpty()) {
            Log.d("MyAppNavHost", "Navigating to note detail $noteId")
            navController.navigate("$itemsRoute/$noteId")
        }
    }
}
