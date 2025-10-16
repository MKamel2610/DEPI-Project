package com.example.ticketway.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ticketway.data.repository.FixturesRepository
import kotlinx.coroutines.launch
import android.util.Log

class FixturesViewModel : ViewModel() {

    private val repository = FixturesRepository()

    fun fetchFixtures(date: String) {
        viewModelScope.launch {
            try {
                val response = repository.getFixtures(date)
                if (response.isSuccessful) {
                    Log.d("FixturesViewModel", "Success: ${response.body()}")
                } else {
                    Log.e("FixturesViewModel", "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("FixturesViewModel", "Exception: ${e.message}")
            }
        }
    }
}
