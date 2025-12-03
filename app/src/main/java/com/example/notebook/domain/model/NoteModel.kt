package com.example.notebook.domain.model

data class Note(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String? = null,
    val createdAt: Long = 0L,
    val edited: Boolean = false
)