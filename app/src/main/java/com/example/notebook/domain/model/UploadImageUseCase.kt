package com.example.notebook.domain.model

import android.net.Uri
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(uri: Uri): String {
        return repository.uploadImage(uri)
    }
}