package com.example.notebook.di

import com.example.notebook.data.model.NoteRepositoryImpl
import com.example.notebook.domain.model.AddNoteUseCase
import com.example.notebook.domain.model.DeleteNoteUseCase
import com.example.notebook.domain.model.GetNotesUseCase
import com.example.notebook.domain.model.NoteRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        impl: NoteRepositoryImpl
    ): NoteRepository
}

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}