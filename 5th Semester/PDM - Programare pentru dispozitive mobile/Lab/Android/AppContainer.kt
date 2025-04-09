package com.example.smartnote

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapp.auth.data.AuthRepository
import com.example.myapp.auth.data.remote.AuthDataSource
import com.example.smartnote.core.TAG
import com.example.smartnote.core.data.UserPreferencesRepository
import com.example.smartnote.core.data.remote.Api
import com.example.smartnote.todo.data.remote.ItemService
import com.example.smartnote.todo.data.remote.ItemWsClient
import com.example.smartnote.todo.ui.item.ItemRepository
import com.example.smartnote.utils.ConnectivityManagerNetworkMonitor


val Context.userPreferencesDataStore by preferencesDataStore(
    name = "user_preferences"
)

class AppContainer(val context: Context) {
    init {
        Log.d(TAG, "init")
    }

    private val itemService: ItemService = Api.retrofit.create(ItemService::class.java)
    private val itemWsClient: ItemWsClient = ItemWsClient(Api.okHttpClient)
    private val authDataSource: AuthDataSource = AuthDataSource()

    private val database: MyAppDatabase by lazy { MyAppDatabase.getDatabase(context) }

    val connectivityManagerNetworkMonitor = ConnectivityManagerNetworkMonitor(context)

    val itemRepository: ItemRepository by lazy {
        ItemRepository(itemService, itemWsClient, database.itemDao(), connectivityManagerNetworkMonitor)
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(authDataSource)
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context.userPreferencesDataStore)
    }
}
