package com.example.notebook.domain.model

import javax.inject.Inject

class GetNotesUseCase @Inject constructor(

    private val repository: NoteRepository
    ) {
        operator fun invoke() = repository.getNotes()
    }
