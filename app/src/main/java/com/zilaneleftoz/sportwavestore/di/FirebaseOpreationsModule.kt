package com.zilaneleftoz.sportwavestore.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

object FirebaseOpreationsModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object FirebaseModule {

        @Provides
        @Singleton
        fun provideFirebaseAuth() = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()
    }
}