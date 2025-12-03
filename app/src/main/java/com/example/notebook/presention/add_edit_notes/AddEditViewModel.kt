package com.example.notebook.presention.add_edit_notes

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebook.domain.model.AddNoteUseCase
import com.example.notebook.domain.model.Note
import com.example.notebook.domain.model.UpdateNoteUseCase
import com.example.notebook.domain.model.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel() {

    // Compose ile bağlayacağımız state'ler
    var title = mutableStateOf("")
        private set
    var description = mutableStateOf("")
        private set
    var imageUri = mutableStateOf<Uri?>(null)
        private set

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    // TextField güncelleme fonksiyonları
    fun onTitleChange(newTitle: String) { title.value = newTitle }
    fun onDescriptionChange(newDescription: String) { description.value = newDescription }
    fun onImageUriChange(newUri: Uri?) { imageUri.value = newUri }

    // Not ekleme
    fun addNote() {
        viewModelScope.launch {
            _isLoading.value = true
            val imageUrl = imageUri.value?.let { uploadImageUseCase(it) }
            val note = Note(
                title = title.value,
                description = description.value,
                imageUrl = imageUrl,
                createdAt = System.currentTimeMillis(),
                edited = false
            )
            addNoteUseCase(note)
            _isLoading.value = false
        }
    }

    // Not güncelleme
    fun updateNote(note: Note) {
        viewModelScope.launch {
            _isLoading.value = true
            val imageUrl = imageUri.value?.let { uploadImageUseCase(it) } ?: note.imageUrl
            val updatedNote = note.copy(
                title = title.value,
                description = description.value,
                imageUrl = imageUrl,
                edited = true
            )
            updateNoteUseCase(updatedNote)
            _isLoading.value = false
        }
    }

    // noteId ile note'u yükle ve state'leri doldur
    fun loadNoteById(noteId: String?) {
        if (noteId.isNullOrEmpty()) return
        val note = getNoteById(noteId)
        note?.let {
            title.value = it.title
            description.value = it.description
            imageUri.value = it.imageUrl?.let { uriStr -> Uri.parse(uriStr) }
        }
    }

    // mevcut notu listeden bul
    fun getNoteById(id: String): Note? {
        return _notes.value.find { it.id == id }
    }
}