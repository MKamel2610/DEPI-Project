package com.example.ticketway.data.repository

import com.example.ticketway.data.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

// Custom exceptions to provide readable error messages from the repository
sealed class AuthException(message: String) : Exception(message) {
    class InvalidCredentials(message: String = "Invalid email or password.") : AuthException(message)
    class WeakPassword(message: String = "Password should be at least 6 characters.") : AuthException(message)
    class UserCollision(message: String = "An account with this email already exists.") : AuthException(message)
    class UnknownError(message: String) : AuthException(message)
}

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    val currentUser get() = auth.currentUser
    val currentUserId get() = auth.currentUser?.uid
    val isLoggedIn get() = auth.currentUser != null

    /**
     * Registers the user in Firebase Auth and creates a placeholder profile in Firestore.
     */
    suspend fun register(email: String, password: String): Result<Unit> {
        return try {
            // Step 1: Create Firebase Auth account
            auth.createUserWithEmailAndPassword(email, password).await()

            // Step 2: Create PLACHOLDER user profile in Firestore (will be updated on ProfileSetupScreen)
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("User ID not found"))
            val userProfile = UserProfile(
                email = email,
                name = "",
                phone = ""
            )

            firestore.collection("users")
                .document(uid)
                .set(userProfile)
                .await()

            Result.success(Unit)
        } catch (e: FirebaseAuthWeakPasswordException) {
            Result.failure(AuthException.WeakPassword())
        } catch (e: FirebaseAuthUserCollisionException) {
            Result.failure(AuthException.UserCollision())
        } catch (e: Exception) {
            Result.failure(AuthException.UnknownError(e.message ?: "Registration failed unexpectedly."))
        }
    }

    /**
     * Updates the name and phone fields on the existing user's Firestore profile.
     */
    suspend fun updateUserProfile(name: String, phone: String): Result<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: return Result.failure(Exception("User not authenticated."))

            val updates = mapOf(
                "name" to name,
                "phone" to phone
            )

            firestore.collection("users")
                .document(uid)
                .update(updates)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(AuthException.UnknownError("Failed to update profile."))
        }
    }


    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(AuthException.InvalidCredentials())
        } catch (e: Exception) {
            Result.failure(AuthException.UnknownError(e.message ?: "Login failed unexpectedly."))
        }
    }

    fun logout() {
        auth.signOut()
    }
}