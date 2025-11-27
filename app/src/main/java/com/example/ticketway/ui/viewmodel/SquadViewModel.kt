package com.example.ticketway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.repository.FootballRepository
import com.example.ticketway.data.network.model.squads.SquadResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.util.Log
import javax.inject.Inject // NEW IMPORT
import dagger.hilt.android.lifecycle.HiltViewModel // NEW IMPORT

@HiltViewModel // NEW ANNOTATION
class SquadViewModel @Inject constructor(private val repository: FootballRepository) : ViewModel() { // NEW ANNOTATION

    private val _squad = MutableStateFlow<SquadResponse?>(null)
    val squad: StateFlow<SquadResponse?> = _squad

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadSquad(teamId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val data = repository.getSquad(teamId)
                _squad.value = data
                Log.d("SquadViewModel", "Loaded squad for team $teamId with ${data?.response?.size ?: 0} items")
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("SquadViewModel", "Error loading squad: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}