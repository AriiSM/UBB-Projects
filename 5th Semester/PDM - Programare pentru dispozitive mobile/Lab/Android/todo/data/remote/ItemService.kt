package com.example.smartnote.todo.data.remote

import com.example.smartnote.todo.data.Note
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ItemService {
    @GET("/api/note")
    suspend fun find(@Header("Authorization") authorization: String): List<Note>

    @GET("/api/note/{id}")
    suspend fun read(
        @Header("Authorization") authorization: String,
        @Path("id") itemId: String?
    ): Note;

    @Headers("Content-Type: application/json")
    @POST("/api/note")
    suspend fun create(@Header("Authorization") authorization: String, @Body item: Note): Note

    @Headers("Content-Type: application/json")
    @PUT("/api/note/{id}")
    suspend fun update(
        @Header("Authorization") authorization: String,
        @Path("id") itemId: String?,
        @Body item: Note
    ): Note
}