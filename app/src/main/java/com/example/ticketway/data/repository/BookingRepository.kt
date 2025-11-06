package com.example.ticketway.data.repository

import com.example.ticketway.data.local.dao.BookingDao
import com.example.ticketway.data.local.entities.BookingEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookingRepository(
    private val bookingDao: BookingDao
) {

    suspend fun bookMatch(booking: BookingEntity) = withContext(Dispatchers.IO) {
        bookingDao.insertBooking(booking)
        // TODO: later, upload to Firebase
    }


    suspend fun getBookingsByUser(userId: String): List<BookingEntity> = withContext(Dispatchers.IO) {
        bookingDao.getBookingsByUser(userId)
    }

    suspend fun clearBookings() = withContext(Dispatchers.IO) {
        bookingDao.clearAll()
    }
}
