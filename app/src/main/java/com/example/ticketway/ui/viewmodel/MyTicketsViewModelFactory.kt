//package com.example.ticketway.ui.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.ticketway.data.repository.BookingRepository
//
//class MyTicketsViewModelFactory(private val bookingRepo: BookingRepository) : ViewModelProvider.Factory {
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(MyTicketsViewModel::class.java)) {
//            return MyTicketsViewModel(bookingRepo) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
