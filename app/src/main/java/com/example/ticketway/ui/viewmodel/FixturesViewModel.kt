package com.example.ticketway.ui.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.repository.FootballRepository
import com.example.ticketway.data.network.model.fixtures.FixtureResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject // NEW IMPORT
import dagger.hilt.android.lifecycle.HiltViewModel // NEW IMPORT

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel // NEW ANNOTATION
class FixturesViewModel @Inject constructor( // NEW ANNOTATION
    private val repository: FootballRepository
) : ViewModel() {

    private val _fixtures = MutableStateFlow<FixtureResponse?>(null)
    val fixtures: StateFlow<FixtureResponse?> = _fixtures

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        // Load 3-day fixtures on initialization
        loadThreeDayFixtures()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadThreeDayFixtures() {
        Log.d("FixturesViewModel", "🔄 Loading 3-day fixtures")

        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val data = repository.getThreeDayFixtures()

                if (data != null) {
                    _fixtures.value = data
                    val count = data.response.size
                    Log.d("FixturesViewModel", "✅ Loaded $count fixtures")

                    // Log fixtures by league
                    data.response.groupBy { it.league.name }.forEach { (league, matches) ->
                        Log.d("FixturesViewModel", "   📊 $league: ${matches.size} matches")
                    }
                } else {
                    _fixtures.value = FixtureResponse(emptyList())
                    Log.w("FixturesViewModel", "No data returned")
                }
            } catch (e: Exception) {
                _error.value = e.message
                _fixtures.value = FixtureResponse(emptyList())
                Log.e("FixturesViewModel", "Error loading fixtures: ${e.message}", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadThreeDayFixtures()
    }
}