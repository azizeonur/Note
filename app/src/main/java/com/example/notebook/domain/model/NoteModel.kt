package com.example.notebook.domain.model

import com.google.firebase.Timestamp

data class Note(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val edited: Boolean = false
)