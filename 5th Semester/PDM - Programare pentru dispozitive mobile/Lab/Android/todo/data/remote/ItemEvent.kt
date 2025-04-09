package com.example.smartnote.todo.data.remote

import com.example.smartnote.todo.data.Note


data class ItemEvent(val type: String, val payload: Note)