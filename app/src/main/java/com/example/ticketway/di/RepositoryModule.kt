package com.example.ticketway.di

import android.content.Context
import com.example.ticketway.data.local.CacheDatabase
import com.example.ticketway.data.local.DatabaseProvider
import com.example.ticketway.data.repository.AuthRepository
import com.example.ticketway.data.repository.BookingRepository
import com.example.ticketway.data.repository.FootballRepository
import com.example.ticketway.data.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    // --- Database & Football Repository ---

    @Singleton
    @Provides
    fun provideCacheDatabase(@ApplicationContext context: Context): CacheDatabase {
        return DatabaseProvider.getDatabase(context)
    }

    @Singleton
    @Provides
    fun provideFootballRepository(db: CacheDatabase): FootballRepository {
        return FootballRepository(db)
    }

    // --- Repositories that rely on Firebase ---

    @Singleton
    @Provides
    fun provideBookingRepository(auth: FirebaseAuth, db: FirebaseFirestore): BookingRepository {
        // Hilt provides FirebaseAuth and FirebaseFirestore from the methods above
        return BookingRepository(auth, db)
    }

    @Singleton
    @Provides
    fun provideAuthRepository(auth: FirebaseAuth, db: FirebaseFirestore): AuthRepository {
        return AuthRepository(auth, db)
    }

    @Singleton
    @Provides
    fun provideUserRepository(auth: FirebaseAuth, db: FirebaseFirestore): UserRepository {
        return UserRepository(auth, db)
    }
}