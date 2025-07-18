package com.example.myapp.auth.data

import android.util.Log
import com.example.myapp.auth.data.remote.AuthDataSource
import com.example.myapp.auth.data.remote.TokenHolder
import com.example.myapp.auth.data.remote.User
import com.example.smartnote.auth.data.local.AuthManager
import com.example.smartnote.core.TAG
import com.example.smartnote.core.data.remote.Api

class AuthRepository(private val authDataSource: AuthDataSource) {
    init {
        Log.d(TAG, "init")
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = authDataSource.login(user)
        if (result.isSuccess) {
            Api.tokenInterceptor.token = result.getOrNull()?.token
        }
        return result
    }
}
