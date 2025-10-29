package com.example.ticketway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.repository.FootballRepository
import com.example.ticketway.data.network.model.standings.StandingsResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log

class StandingsViewModel(
    private val repository: FootballRepository
) : ViewModel() {

    private val _standings = MutableStateFlow<StandingsResponse?>(null)
    val standings: StateFlow<StandingsResponse?> = _standings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadStandings(leagueId: Int, season: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = repository.getStandings(leagueId, season)
                _standings.value = data
                Log.d("StandingsViewModel", "Loaded ${data?.response?.size ?: 0} standings")
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("StandingsViewModel", "Error loading standings: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
