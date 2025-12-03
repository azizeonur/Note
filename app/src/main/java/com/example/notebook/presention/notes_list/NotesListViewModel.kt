package com.example.notebook.presention.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notebook.domain.model.DeleteNoteUseCase
import com.example.notebook.domain.model.GetNotesUseCase
import com.example.notebook.domain.model.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesListViewModel @Inject constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        loadNotes()
    }

    private fun loadNotes() {
        viewModelScope.launch {
            getNotesUseCase().collectLatest {
                _notes.value = it.sortedByDescending { note -> note.createdAt }
            }
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            deleteNoteUseCase(id)
        }
    }
}