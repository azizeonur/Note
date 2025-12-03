package com.example.notebook.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class NoteDto(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    @ServerTimestamp
    val createdAt: Date? = null,
    val edited: Boolean = false
)