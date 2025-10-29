package com.example.ticketway.data.local

import androidx.room.TypeConverter

class Converters {

    // You can expand this later as needed.
    @TypeConverter
    fun fromList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toList(value: String?): List<String>? {
        return value?.split(",")
    }
}
