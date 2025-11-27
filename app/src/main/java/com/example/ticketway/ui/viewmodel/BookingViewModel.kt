package com.example.ticketway.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.model.BookingItem
import com.example.ticketway.data.model.UserProfile
import com.example.ticketway.data.repository.BookingRepository
import com.example.ticketway.data.network.model.fixtures.FixtureItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

// Define Prices (using existing values)
private const val REGULAR_PRICE = 100
private const val PREMIUM_PRICE = 200
private const val MAX_TICKETS = 4

class BookingViewModel(
    private val repo: BookingRepository
) : ViewModel() {

    // --- State fields removed: _paymobUrl, _currentBookingId ---

    private val _currentFixture = MutableStateFlow<FixtureItem?>(null)
    val currentFixture: StateFlow<FixtureItem?> = _currentFixture.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private val _errorMsg = MutableStateFlow<String?>(null)
    val errorMsg: StateFlow<String?> = _errorMsg.asStateFlow()

    private val _regularCount = MutableStateFlow(0)
    val regularCount: StateFlow<Int> = _regularCount.asStateFlow()

    private val _premiumCount = MutableStateFlow(0)
    val premiumCount: StateFlow<Int> = _premiumCount.asStateFlow()

    val totalTickets: StateFlow<Int> = combine(_regularCount, _premiumCount) { reg, prem ->
        reg + prem
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    val totalPrice: StateFlow<Int> = combine(_regularCount, _premiumCount) { reg, prem ->
        (reg * REGULAR_PRICE) + (prem * PREMIUM_PRICE)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    // --- Actions ---

    fun startBooking(fixture: FixtureItem) {
        _currentFixture.value = fixture
        clearSelections()
    }

    fun updateSelection(tier: String, newCount: Int) {
        if (newCount < 0) return

        _errorMsg.value = null
        val currentReg = _regularCount.value
        val currentPrem = _premiumCount.value

        val newTotal = when (tier) {
            "Regular" -> currentPrem + newCount
            "Premium" -> currentReg + newCount
            else -> currentReg + currentPrem
        }

        if (newTotal <= MAX_TICKETS) {
            when (tier) {
                "Regular" -> _regularCount.value = newCount
                "Premium" -> _premiumCount.value = newCount
            }
        } else {
            val ticketsOtherThanCurrentTier = if (tier == "Regular") currentPrem else currentReg
            val maxCountForTier = MAX_TICKETS - ticketsOtherThanCurrentTier

            val finalCount = if (newCount > maxCountForTier) maxCountForTier else newCount

            when (tier) {
                "Regular" -> _regularCount.value = finalCount
                "Premium" -> _premiumCount.value = finalCount
            }
            _errorMsg.value = "Maximum of $MAX_TICKETS tickets allowed. Tickets adjusted."
        }
    }

    fun clearSelections() {
        _regularCount.value = 0
        _premiumCount.value = 0
    }

    fun clearBookingState() {
        clearSelections()
        _currentFixture.value = null
        _errorMsg.value = null
        _isSuccess.value = false
        _isLoading.value = false
    }

    /**
     * NEW: Finalizes the booking by setting status to PAID and saving to Firestore.
     */
    fun finalizeBookingAndSave() {
        val fixture = _currentFixture.value ?: return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMsg.value = null

            // 1. Create Final Booking Item
            val bookingToSave = createFinalBookingItem(fixture)

            // 2. Save to Firestore
            val isSaved = repo.saveBooking(bookingToSave)

            _isLoading.value = false

            if (isSaved) {
                _isSuccess.value = true
                _errorMsg.value = null
            } else {
                _isSuccess.value = false
                _errorMsg.value = "Payment confirmed, but failed to save booking details to Firebase."
            }
        }
    }

    private fun createFinalBookingItem(fixture: FixtureItem): BookingItem {
        val total = totalPrice.value
        return BookingItem(
            bookingId = UUID.randomUUID().toString(), // Generate new unique ID
            fixtureId = fixture.fixture.id,
            homeTeam = fixture.teams.home.name,
            awayTeam = fixture.teams.away.name,
            stadiumName = fixture.fixture.venue?.name ?: "Venue TBA",
            matchDate = fixture.fixture.date,
            seatCount = totalTickets.value,
            regularCount = _regularCount.value,
            premiumCount = _premiumCount.value,
            totalPrice = total,
            paymentStatus = "PAID" // Mocked success
        )
    }
}