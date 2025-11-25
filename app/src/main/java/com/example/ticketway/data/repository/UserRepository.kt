package com.example.ticketway.data.repository

import com.example.ticketway.data.firebase.FirebaseRefs
import com.example.ticketway.data.model.UserProfile
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val users = FirebaseRefs.db.collection("users")
    private val auth = FirebaseRefs.auth

    suspend fun saveUserProfile(profile: UserProfile) {
        val uid = auth.currentUser?.uid ?: return
        users.document(uid).set(profile).await()
    }

    suspend fun getUserProfile(): UserProfile? {
        val uid = auth.currentUser?.uid ?: return null
        val snapshot = users.document(uid).get().await()
        return snapshot.toObject(UserProfile::class.java)
    }
}