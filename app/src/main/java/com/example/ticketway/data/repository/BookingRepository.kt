package com.example.ticketway.data.repository

import com.example.ticketway.data.model.BookingItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject // NEW IMPORT

class BookingRepository @Inject constructor( // ADD @Inject constructor
    private val auth: FirebaseAuth, // Injected dependency
    private val db: FirebaseFirestore // Injected dependency
) {
    // Note: We are using injected parameters instead of the old FirebaseRefs object

    suspend fun saveBooking(booking: BookingItem): Boolean {
        return try {
            val uid = auth.currentUser?.uid ?: return false
            // Use the provided bookingId (usually a UUID generated in the ViewModel)
            val id = booking.bookingId.ifEmpty { db.collection("bookings").document().id }

            val safeBooking = booking.copy(
                bookingId = id,
                userId = uid
            )

            db.collection("bookings").document(id).set(safeBooking).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserBookings(): List<BookingItem> {
        return try {
            val uid = auth.currentUser?.uid ?: return emptyList()
            db.collection("bookings").whereEqualTo("userId", uid)
                .get()
                .await()
                .toObjects(BookingItem::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}