package com.example.ticketway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ticketway.data.repository.BookingRepository
import com.example.ticketway.ui.booking.BookingViewModel

class BookingViewModelFactory(private val bookingRepo: BookingRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookingViewModel::class.java)) {
            return BookingViewModel(bookingRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}