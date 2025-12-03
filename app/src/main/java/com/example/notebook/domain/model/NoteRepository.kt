package com.example.notebook.domain.model

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun addNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(id: String)

    suspend fun uploadImage(uri: Uri): String
}