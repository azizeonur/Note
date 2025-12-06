package com.example.notebook.presention.add_edit_notes

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebook.domain.model.AddNoteUseCase
import com.example.notebook.domain.model.DeleteNoteUseCase
import com.example.notebook.domain.model.Note
import com.example.notebook.domain.model.UpdateNoteUseCase
import com.example.notebook.domain.model.UploadImageUseCase
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class AddEditNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    var title = mutableStateOf("")
        private set
    var description = mutableStateOf("")
        private set

    private var currentNote: Note? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun onTitleChange(v: String) { title.value = v }
    fun onDescriptionChange(v: String) { description.value = v }

    fun loadNoteById(noteId: String?) {
        if (noteId.isNullOrEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val snapshot = FirebaseFirestore.getInstance()
                    .collection("notes")
                    .document(noteId)
                    .get()
                    .await()

                val note = snapshot.toObject(Note::class.java)
                if (note != null) {
                    currentNote = note
                    title.value = note.title
                    description.value = note.description
                }
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun addNote() {
        viewModelScope.launch {
            val note = Note(
                id = "",
                title = title.value,
                description = description.value,
                createdAt = Timestamp.now(),
                edited = false
            )
            addNoteUseCase(note)
        }
    }

    fun updateNote() {
        val original = currentNote ?: return

        viewModelScope.launch {
            val updated = original.copy(
                title = title.value,
                description = description.value,
                edited = true
            )
            updateNoteUseCase(updated)
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val isTitleEmpty = title.value.isBlank()
                val isDescEmpty = description.value.isBlank()

                val note = currentNote

                if (note != null && isTitleEmpty && isDescEmpty) {
                    deleteNoteUseCase(note.id)
                    return@launch
                }

                if (note == null) {
                    addNote()
                    return@launch
                }

                updateNote()

            } finally {
                _isLoading.value = false
            }
        }
    }
}