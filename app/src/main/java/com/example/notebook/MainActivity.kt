package com.example.notebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notebook.presention.add_edit_notes.AddEditNoteScreen
import com.example.notebook.presention.notes_list.NotesListScreen
import com.example.notebook.ui.theme.NoteBookTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteBookTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "notes_list") {


                    composable("notes_list") {
                        NotesListScreen(
                            viewModel = hiltViewModel(),
                            onAddNote = { navController.navigate("add_edit_note") },
                            onEditNote = { note ->
                                navController.navigate("add_edit_note?noteId=${note.id}")
                            }
                        )
                    }


                    composable(
                        route = "add_edit_note?noteId={noteId}",
                        arguments = listOf(
                            navArgument("noteId") {
                                type = NavType.StringType
                                defaultValue = ""
                                nullable = true
                            }
                        )
                    ) { backStackEntry ->
                        val noteId = backStackEntry.arguments?.getString("noteId")
                        AddEditNoteScreen(
                            noteId = noteId?.takeIf { it.isNotEmpty() },
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}