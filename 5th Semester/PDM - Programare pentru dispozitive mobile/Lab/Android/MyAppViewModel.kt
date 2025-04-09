package com.example.smartnote

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.smartnote.core.TAG
import com.example.smartnote.core.data.UserPreferences
import com.example.smartnote.core.data.UserPreferencesRepository
import com.example.smartnote.todo.data.Note
import com.example.smartnote.todo.ui.item.ItemRepository
import kotlinx.coroutines.launch
import kotlin.random.Random


class MyAppViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val itemRepository: ItemRepository
) :
    ViewModel() {

    init {
        Log.d(TAG, "init")
    }

    fun logout() {
        viewModelScope.launch {
            itemRepository.deleteAll()
            userPreferencesRepository.save(UserPreferences())
        }
    }

    fun setToken(token: String) {
        itemRepository.setToken(token)
    }

    fun addItem(titlu: String, descriere: String, data: String, prioritate: String, complet: Boolean, imageUri: String?) {
        viewModelScope.launch {
            val id = Random.nextInt(1, 101)
            itemRepository.save(
                Note(
                    id.toString(),
                    titlu,
                    descriere,
                    data,
                    Integer.valueOf(prioritate),
                    complet,
                    imageUri = imageUri  // Adăugăm imaginea
                )
            )
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                MyAppViewModel(
                    app.container.userPreferencesRepository,
                    app.container.itemRepository
                )
            }
        }
    }
}
