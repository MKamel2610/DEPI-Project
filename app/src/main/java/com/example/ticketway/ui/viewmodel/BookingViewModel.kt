package com.example.ticketway.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.local.entities.BookingEntity
import com.example.ticketway.data.repository.BookingRepository
import kotlinx.coroutines.launch
import java.util.UUID

class BookingViewModel(
    private val repository: BookingRepository
) : ViewModel() {

    private val _selectedFixtureId = MutableLiveData<Int>()
    val selectedFixtureId: LiveData<Int> = _selectedFixtureId

    private val _matchTitle = MutableLiveData<String>()
    val matchTitle: LiveData<String> = _matchTitle

    private val _stadiumName = MutableLiveData<String>()
    val stadiumName: LiveData<String> = _stadiumName

    private val _matchDate = MutableLiveData<String>()
    val matchDate: LiveData<String> = _matchDate

    private val _seatTier = MutableLiveData<String>()
    val seatTier: LiveData<String> = _seatTier

    private val _seatCount = MutableLiveData<Int>()
    val seatCount: LiveData<Int> = _seatCount

    private val _totalPrice = MutableLiveData<Double>()
    val totalPrice: LiveData<Double> = _totalPrice

    private val _bookingResult = MutableLiveData<Boolean>()
    val bookingResult: LiveData<Boolean> = _bookingResult

    // Example pricing per tier (can be dynamic later)
    private val tierPrices = mapOf(
        "Regular" to 100.0,
        "VIP" to 200.0,
        "Premium" to 300.0
    )

    fun selectFixture(fixtureId: Int, title: String, stadium: String, date: String) {
        _selectedFixtureId.value = fixtureId
        _matchTitle.value = title
        _stadiumName.value = stadium
        _matchDate.value = date
    }

    fun selectTier(tier: String) {
        _seatTier.value = tier
        updateTotal()
    }

    fun selectSeatCount(count: Int) {
        _seatCount.value = count
        updateTotal()
    }

    private fun updateTotal() {
        val tier = _seatTier.value ?: return
        val count = _seatCount.value ?: return
        val pricePerSeat = tierPrices[tier] ?: 0.0
        _totalPrice.value = pricePerSeat * count
    }

    fun confirmBooking(userId: String) {
        val fixtureId = _selectedFixtureId.value ?: return
        val title = _matchTitle.value ?: return
        val stadium = _stadiumName.value ?: return
        val date = _matchDate.value ?: return
        val tier = _seatTier.value ?: return
        val count = _seatCount.value ?: return
        val total = _totalPrice.value ?: return
        val pricePerSeat = tierPrices[tier] ?: 0.0

        val booking = BookingEntity(
            bookingId = UUID.randomUUID().toString(),
            fixtureId = fixtureId,
            userId = userId,
            matchTitle = title,
            stadiumName = stadium,
            matchDate = date,
            seatTier = tier,
            seatCount = count,
            pricePerSeat = pricePerSeat,
            totalPrice = total,
            paymentStatus = "Pending"
        )

        viewModelScope.launch {
            repository.bookMatch(booking)
            _bookingResult.postValue(true)
        }
    }
}
