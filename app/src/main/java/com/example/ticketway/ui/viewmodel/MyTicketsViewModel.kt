package com.example.ticketway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyTicketsViewModel(
    private val repo: BookingRepository = BookingRepository()
) : ViewModel() {

    private val _tickets = MutableStateFlow<List<BookingItem>>(emptyList())
    val tickets: StateFlow<List<BookingItem>> = _tickets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        loadTickets()
    }

    fun loadTickets() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val allBookings = repo.getUserBookings()

                // Filter only for successfully paid tickets
                val paidTickets = allBookings.filter { it.paymentStatus == "PAID" }

                _tickets.value = paidTickets

                if (paidTickets.isEmpty() && allBookings.isNotEmpty()) {
                    _errorMessage.value = "You have bookings, but none are fully paid."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load tickets: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}