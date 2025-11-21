package com.example.ticketway.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.data.repository.BookingRepository
import kotlinx.coroutines.launch

class BookingViewModel(
    private val repo: BookingRepository = BookingRepository()
) : ViewModel() {

    fun saveBooking(booking: BookingItem, onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            repo.saveBooking(booking)
            onSaved()
        }
    }
}
