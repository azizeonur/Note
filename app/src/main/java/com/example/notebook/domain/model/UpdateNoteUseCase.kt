package com.example.notebook.domain.model

import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        repository.updateNote(note)
    }
}