package com.example.ticketway.data.repository

import com.example.ticketway.data.firebase.FirebaseRefs
import com.example.ticketway.data.model.BookingItem
import kotlinx.coroutines.tasks.await

class BookingRepository {

    private val bookings = FirebaseRefs.db.collection("bookings")
    private val auth = FirebaseRefs.auth

    suspend fun saveBooking(booking: BookingItem) {
        bookings.document(booking.bookingId).set(booking).await()
    }

    suspend fun getUserBookings(): List<BookingItem> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        val snapshot = bookings.whereEqualTo("userId", uid).get().await()
        return snapshot.toObjects(BookingItem::class.java)
    }
}
