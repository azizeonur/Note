package com.example.notebook.presention.notes_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import coil.compose.rememberAsyncImagePainter

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.notebook.domain.model.Note


@Composable
fun NotesListScreen(
    viewModel: NotesListViewModel = hiltViewModel(),
    onAddNote: () -> Unit,
    onEditNote: (Note) -> Unit
) {
    val notes = viewModel.notes.collectAsState().value

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddNote) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(notes) { note ->
                NoteItem(
                    note = note,
                    onEdit = { onEditNote(note) },
                    onDelete = { viewModel.deleteNote(note.id) }
                )
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onEdit() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            if (!note.imageUrl.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(note.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .padding(end = 8.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = note.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2
                )
                if (note.edited) {
                    Text(
                        text = "(edited)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}