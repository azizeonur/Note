package com.example.notebook.domain.model

import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: String) {
        repository.deleteNote(id)
    }
}