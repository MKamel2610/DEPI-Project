package com.example.ticketway.data.repository

import com.example.ticketway.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject // NEW IMPORT

class UserRepository @Inject constructor( // ADD @Inject constructor
    private val auth: FirebaseAuth, // Injected dependency
    private val db: FirebaseFirestore // Injected dependency
) {
    // Note: If you only need 'db' and 'auth' is inferred, you can make this cleaner, but this is explicit.

    suspend fun saveUserProfile(profile: UserProfile) {
        val uid = auth.currentUser?.uid ?: return
        db.collection("users").document(uid).set(profile).await()
    }

    suspend fun getUserProfile(): UserProfile? {
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = db.collection("users").document(uid).get().await()
        return snapshot.toObject(UserProfile::class.java)
    }
}