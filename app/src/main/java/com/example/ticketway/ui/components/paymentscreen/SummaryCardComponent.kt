package com.example.ticketway.ui.components.paymentscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SummaryCard(
    total: Int
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("Total Price", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("$total EGP", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF009688))
        }
    }
}
