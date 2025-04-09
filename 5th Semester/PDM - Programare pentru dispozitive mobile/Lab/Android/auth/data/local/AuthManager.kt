package com.example.smartnote.auth.data.local

import android.util.Log
import com.auth0.android.jwt.JWT
import com.example.smartnote.core.data.remote.Api

object AuthManager {
    private const val TAG = "AuthManager"

    // Funcție pentru a extrage userID din token
    fun getUserIdFromToken(): String? {
        // Tokenul trebuie să fie deja disponibil în Api.tokenInterceptor.token
        val token = Api.tokenInterceptor.token
        return try {
            // Folosește constructorul JWT pentru a decoda tokenul
            val decodedJWT = JWT(token.toString())
            decodedJWT.getClaim("userID").asString() // Extrage userID din token
        } catch (e: Exception) {
            Log.e(TAG, "Error extracting userID from token", e)
            null
        }
    }
}
