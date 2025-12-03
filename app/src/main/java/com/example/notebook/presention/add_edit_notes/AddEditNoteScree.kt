package com.example.notebook.presention.add_edit_notes


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.notebook.domain.model.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    viewModel: AddEditNoteViewModel = hiltViewModel(),
    noteId: String? = null,
    onBack: () -> Unit
) {
    // noteId varsa ViewModel’e yükle
    LaunchedEffect(noteId) {
        viewModel.loadNoteById(noteId)
    }

    val title by viewModel.title
    val description by viewModel.description
    val imageUri by viewModel.imageUri
    val isLoading by viewModel.isLoading.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageUriChange(uri)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (noteId == null) "Add Note" else "Edit Note") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = viewModel::onTitleChange,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = viewModel::onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = { launcher.launch("image/*") }) {
                Text("Select Image")
            }

            val painter = imageUri?.let { rememberAsyncImagePainter(it) }
            painter?.let {
                Image(painter = it, contentDescription = null, modifier = Modifier.size(128.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (noteId == null) {
                        viewModel.addNote()
                    } else {
                        viewModel.getNoteById(noteId)?.let { note ->
                            viewModel.updateNote(note)
                        }
                    }
                    onBack()
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }
        }
    }
}