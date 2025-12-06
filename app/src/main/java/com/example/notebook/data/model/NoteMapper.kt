package com.example.notebook.data.model

import com.example.notebook.domain.model.Note
import com.google.firebase.Timestamp

object NoteMapper {

    fun fromDto(dto: NoteDto): Note {
        return Note(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            imageUrl = dto.imageUrl,
            createdAt = Timestamp.now(),
            edited = dto.edited
        )
    }

    fun toDto(note: Note): NoteDto {
        return NoteDto(
            id = note.id,
            title = note.title,
            description = note.description,
            imageUrl = note.imageUrl,
            edited = note.edited
        )
    }
}