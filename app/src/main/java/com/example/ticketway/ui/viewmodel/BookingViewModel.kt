package com.example.ticketway.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BookingViewModel(
    private val repo: BookingRepository = BookingRepository()
) : ViewModel() {

    // ---- UI States ----
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg

    // ---- Ticket selection ----
    var regularCount = 0
    var premiumCount = 0
    var vipCount = 0

    fun updateRegular(count: Int) { regularCount = count }
    fun updatePremium(count: Int) { premiumCount = count }
    fun updateVip(count: Int) { vipCount = count }

    fun getTotalPrice(): Int = (regularCount*100) + (premiumCount*200) + (vipCount*300)

    // ---- Temporary Booking ----
    var tempBooking: TempBooking? = null

    data class TempBooking(
        val fixtureId: Int,
        val homeTeam: String,
        val awayTeam: String,
        val stadiumName: String,
        val matchDate: String,
        val regularCount: Int,
        val premiumCount: Int,
        val vipCount: Int,
        val totalPrice: Int
    )

    open fun clearTempBooking() {
        tempBooking = null
        regularCount = 0
        premiumCount = 0
        vipCount = 0
    }

    // ---- Bookings ----
    private val _userBookings = MutableStateFlow<List<BookingItem>>(emptyList())
    val userBookings: StateFlow<List<BookingItem>> = _userBookings

    fun fetchUserBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            _userBookings.value = repo.getUserBookings()
            _isLoading.value = false
        }
    }

    fun saveBooking(
        fixtureId: Int,
        homeTeam: String,
        awayTeam: String,
        stadiumName: String,
        matchDate: String
    ) {
        val total = getTotalPrice()
        if (total == 0) {
            _errorMsg.value = "Can't save empty booking"
            return
        }

        val booking = BookingItem(
            fixtureId = fixtureId,
            homeTeam = homeTeam,
            awayTeam = awayTeam,
            stadiumName = stadiumName,
            matchDate = matchDate,
            seatCount = regularCount + premiumCount + vipCount,
            regularCount = regularCount,
            premiumCount = premiumCount,
            vipCount = vipCount,
            totalPrice = total
        )

        viewModelScope.launch {
            _isLoading.value = true
            val success = repo.saveBooking(booking)
            _isLoading.value = false
            _isSuccess.value = success
            if (!success) _errorMsg.value = "Error,Try again later"
            else clearTempBooking()
        }
    }
}



