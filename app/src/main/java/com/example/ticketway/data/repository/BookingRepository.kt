package com.example.ticketway.data.repository

import com.example.ticketway.data.firebase.FirebaseRefs
import com.example.ticketway.data.model.BookingItem
import kotlinx.coroutines.tasks.await

class BookingRepository {

    val auth = FirebaseRefs.auth
    private val bookings = FirebaseRefs.db.collection("bookings")

    suspend fun saveBooking(booking: BookingItem): Boolean {
        return try {
            val uid = auth.currentUser?.uid ?: return false
            // Use the provided bookingId (usually a UUID generated in the ViewModel)
            val id = booking.bookingId.ifEmpty { bookings.document().id }

            val safeBooking = booking.copy(
                bookingId = id,
                userId = uid
            )

            bookings.document(id).set(safeBooking).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserBookings(): List<BookingItem> {
        return try {
            val uid = auth.currentUser?.uid ?: return emptyList()
            bookings.whereEqualTo("userId", uid)
                .get()
                .await()
                .toObjects(BookingItem::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}