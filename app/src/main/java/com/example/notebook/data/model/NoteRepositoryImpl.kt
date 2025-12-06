package com.example.notebook.data.model

import android.net.Uri
import com.example.notebook.domain.model.Note
import com.example.notebook.domain.model.NoteRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject


class NoteRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : NoteRepository {

    private val notesCollection = firestore.collection("notes")

    override fun getNotes(): Flow<List<Note>> = callbackFlow {
        val subscription = notesCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val notes = snapshot?.documents?.mapNotNull {
                val dto = it.toObject(NoteDto::class.java)
                dto?.copy(id = it.id)
            }?.map { NoteMapper.fromDto(it) } ?: emptyList()

            trySend(notes)
        }
        awaitClose { subscription.remove() }
    }

    override suspend fun addNote(note: Note) {
        val docRef =
            if (note.id.isBlank()) notesCollection.document()
            else notesCollection.document(note.id)


        val uploadedUrl = uploadImageIfNeeded(note.imageUrl)

        val finalNote = note.copy(
            id = docRef.id,
            imageUrl = uploadedUrl
        )

        docRef.set(NoteMapper.toDto(finalNote)).await()
    }

    override suspend fun updateNote(note: Note) {


        val uploadedUrl = uploadImageIfNeeded(note.imageUrl)

        val finalNote = note.copy(
            imageUrl = uploadedUrl
        )

        notesCollection.document(note.id)
            .set(NoteMapper.toDto(finalNote))
            .await()
    }

    override suspend fun deleteNote(id: String) {
        notesCollection.document(id).delete().await()
    }

    override suspend fun uploadImage(uri: Uri): String {
        val ref = storage.reference.child("notes/${UUID.randomUUID()}.jpg")
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    private suspend fun uploadImageIfNeeded(imageUrl: String?): String? {
        if (imageUrl.isNullOrBlank()) return null

        return if (imageUrl.startsWith("content://")) {
            uploadImage(Uri.parse(imageUrl))
        } else {
            imageUrl
        }
    }
}