package com.example.smartnote.todo.ui.item

import android.util.Log
import com.example.smartnote.auth.data.local.AuthManager.getUserIdFromToken
import com.example.smartnote.core.data.remote.Api
import com.example.smartnote.core.TAG
import com.example.smartnote.todo.data.Note
import com.example.smartnote.todo.data.local.ItemDao
import com.example.smartnote.todo.data.remote.ItemEvent
import com.example.smartnote.todo.data.remote.ItemService
import com.example.smartnote.todo.data.remote.ItemWsClient
import com.example.smartnote.utils.ConnectivityManagerNetworkMonitor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext


class ItemRepository(
    private val itemService: ItemService,
    private val itemWsClient: ItemWsClient,
    private val itemDao: ItemDao,
    private val networkMonitor: ConnectivityManagerNetworkMonitor

) {

    val itemStream by lazy {
        itemDao.getAll()
            .onEach { notes ->
                Log.d(TAG, "Data extracted from DB: $notes")
            }
    }

    init {
        Log.d(TAG, "init")
    }
    private fun getBearerToken() = "Bearer ${Api.tokenInterceptor.token}"

    suspend fun refresh() {
        Log.d(TAG, "refresh started")
        try {
            val items = itemService.find(authorization = getBearerToken())
//            items.forEach { item ->
//                Log.d("items", item.toString())
//            }
            itemDao.deleteAll()
            items.forEach { item->
                itemDao.insert(item) }
            Log.d(TAG, "refresh succeeded")
        } catch (e: Exception) {
            Log.w(TAG, "refresh failed", e)
        }
    }


    suspend fun openWsClient() {
        Log.d(TAG, "openWsClient")
        withContext(Dispatchers.IO) {
            getItemEvents().collect {
                Log.d(TAG, "Item event collected $it")
                if (it.isSuccess) {
                    val itemEvent = it.getOrNull();
                    when (itemEvent?.type) {
                        "created" -> handleItemCreated(itemEvent.payload)
                        "updated" -> handleItemUpdated(itemEvent.payload)
                        "deleted" -> handleItemDeleted(itemEvent.payload)
                    }
                }
            }
        }
    }

    suspend fun closeWsClient() {
        Log.d(TAG, "closeWsClient")
        withContext(Dispatchers.IO) {
            itemWsClient.closeSocket()
        }
    }

    suspend fun getItemEvents(): Flow<Result<ItemEvent>> = callbackFlow {
        itemWsClient.openSocket(
            onEvent = {
                Log.d(TAG, "onEvent received: $it")
                it?.let { event ->
                    trySend(Result.success(event))
                }
            },
            onClosed = { close() },
            onFailure = { close() }
        )
        awaitClose { itemWsClient.closeSocket() }
    }


    suspend fun update(item: Note): Note {
        Log.d(TAG, "update $item...")
        val updatedItem =
            itemService.update(itemId = item.id, item = item, authorization = getBearerToken())
        Log.d(TAG, "update $item succeeded")
        handleItemUpdated(updatedItem)
        return updatedItem
    }

    fun getUnsyncedNotes(): Flow<List<Note>> {
        return itemDao.getAll()
            .map { notes ->
                notes.filter { !it.isSynced } // Filtrăm notele nesincronizate
            }
    }

    suspend fun syncUnsyncedNotesIfOnline() {
        // Verificăm dacă suntem online
        val isOnline = networkMonitor.isOnline
            .conflate() // Optimizăm fluxul
            .first() // Luăm primul eveniment pentru statusul curent al conexiunii

        if (isOnline) {
            // Obținem notele nesincronizate
            getUnsyncedNotes().collect { unsyncedNotes ->
                // Sincronizăm fiecare notă cu serverul
                for (note in unsyncedNotes) {
                    try {
                        // Sincronizare notă cu serverul
                        note.isSynced = true
                        val syncedNote = itemService.create(item = note, authorization = getBearerToken())

//                        val notificationId = UUID.randomUUID().hashCode()
//                        showSimpleNotification(
//                            context = context,
//                            channelId = "MyTestChannel",
//                            notificationId = notificationId,
//                            textTitle = "Titlu: ${note.titlu}",
//                            textContent = "ACUM S-A SALVAT SI PE SERVER, nicio grija !s"
//                        )

                        // După ce notele sunt sincronizate cu serverul, actualizăm starea isSynced
                        itemDao.update(note)
                        Log.d(TAG, "Note synchronized with server: $syncedNote")
                    } catch (e: Exception) {
                        Log.w(TAG, "Failed to sync note with server", e)
                    }
                }
            }
        } else {
            Log.d(TAG, "Device is offline. Syncing skipped.")
        }
    }


    suspend fun save(item: Note): Note {
        Log.d(TAG, "save $item...")
        val userId = getUserIdFromToken() // Funcția ta pentru a extrage userID-ul

        // Verificăm conexiunea la internet folosind ConnectivityManagerNetworkMonitor
        val isOnline = networkMonitor.isOnline
            .conflate() // Optimizăm fluxul
            .first() // Luăm primul eveniment pentru statusul curent al conexiunii

        return if (isOnline) {
            // Dacă există conexiune, salvăm direct pe server
            item.isSynced = true;
            val createdItem = itemService.create(item = item, authorization = getBearerToken())
            Log.d(TAG, "save $item succeeded")
            handleItemCreated(createdItem)
            createdItem
        } else {
            // Dacă nu există conexiune, salvăm local cu `isSynced = false`
            val localItem = item.copy(isSynced = false, userID = userId)

            itemDao.insert(localItem)
            Log.d(TAG, "save $item local only (no internet)")
            localItem
        }
    }


    private suspend fun handleItemDeleted(item: Note) {
        Log.d(TAG, "handleItemDeleted - todo $item")
    }

    private suspend fun handleItemUpdated(item: Note) {
        Log.d(TAG, "handleItemUpdated...")
        itemDao.update(item)
    }

    private suspend fun handleItemCreated(item: Note) {
        Log.d(TAG, "handleItemCreated...")
        itemDao.insert(item)
    }

    suspend fun deleteAll() {
        itemDao.deleteAll()
    }

    fun setToken(token: String) {
        itemWsClient.authorize(token)
    }
}