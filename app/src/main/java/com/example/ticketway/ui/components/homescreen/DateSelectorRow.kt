package com.example.ticketway.ui.components.homescreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

//val PrimaryGreen = Color(0xFF009688)
val LightGray = Color(0xFFF5F5F5)
//val BorderGray = Color(0xFFE0E0E0)
//val DarkText = Color(0xFF212121)
val LightText = Color(0xFF757575)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateSelectorRow(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onCalendarClick: () -> Unit = {}
) {
    val today = LocalDate.now()
    val dates = (-7..7).map { today.plusDays(it.toLong()) }
    val listState = rememberLazyListState()

    // Start with 6th date button as leftmost (today will be in middle)
    LaunchedEffect(Unit) {
        listState.scrollToItem(5) // Index 5, so today (index 7) appears in middle
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // LIVE badge (static, same size as calendar)
        LiveBadge(onClick = { onDateSelected(today) })

        // Scrollable dates - calculate width for exactly 5 buttons
        LazyRow(
            state = listState,
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dates.size) { index ->
                val date = dates[index]
                DateChip(
                    date = date,
                    isSelected = date == selectedDate,
                    isToday = date == today,
                    onClick = { onDateSelected(date) }
                )
            }
        }

        // Calendar icon (static)
        CalendarButton(onClick = onCalendarClick)
    }
}

@Composable
fun LiveBadge(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = LightGray
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(PrimaryGreen)
                )
                Text(
                    text = "LIVE",
                    color = PrimaryGreen,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateChip(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    onClick: () -> Unit
) {
    val dayName = date.format(DateTimeFormatter.ofPattern("EEE"))
    val dayNumber = date.dayOfMonth.toString()

    Surface(
        modifier = Modifier
            .width(56.dp) // Fixed width for exactly 5 buttons to fit
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = BorderGray,
                shape = RoundedCornerShape(12.dp)
            ),
        color = if (isSelected) PrimaryGreen else Color.White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isToday && !isSelected) "Tod" else dayName.take(3),
                color = if (isSelected) Color.White else LightText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = dayNumber,
                color = if (isSelected) Color.White else DarkText,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CalendarButton(onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        color = LightGray
    ) {
        Icon(
            Icons.Default.DateRange,
            contentDescription = "Calendar",
            tint = PrimaryGreen,
            modifier = Modifier.padding(12.dp)
        )
    }
}