package com.example.smartnote.todo.ui.item

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.smartnote.MyApplication
import com.example.smartnote.core.TAG
import com.example.smartnote.core.Result
import com.example.smartnote.todo.data.Note
import kotlinx.coroutines.launch

// State management for Item screen
data class ItemUiState(
    val itemId: String? = null,
    val item: Note = Note(),
    var loadResult: Result<Note>? = null,
    var submitResult: Result<Note>? = null,
)


// ViewModel for Item screen
class ItemViewModel(private val itemId: String?, private val itemRepository: ItemRepository) :
    ViewModel() {

    var uiState: ItemUiState by mutableStateOf(ItemUiState(loadResult = Result.Loading))
        private set

    init {
        Log.d(TAG, "init")
        if (itemId != null) {
            loadItem()
        } else {
            uiState = uiState.copy(loadResult = Result.Success(Note()))
        }
    }


    // Load item from repository
    fun loadItem() {
        viewModelScope.launch {
            itemRepository.itemStream.collect { items ->
                if (!(uiState.loadResult is Result.Loading)) {
                    return@collect
                }
                val item = items.find { it.id == itemId } ?: Note()
                uiState = uiState.copy(item = item, loadResult = Result.Success(item))
            }
        }
    }


    companion object {
        fun Factory(itemId: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyApplication)
                ItemViewModel(itemId, app.container.itemRepository)
            }
        }
    }
}

