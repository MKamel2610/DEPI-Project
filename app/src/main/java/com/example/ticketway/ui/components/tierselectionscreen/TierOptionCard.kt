package com.example.ticketway.ui.components.tierselectionscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val PrimaryGreen = Color(0xFF009688)
val DarkText = Color(0xFF9F9F9F)

@Composable
fun TierOptionCard(
    title: String,
    price: Int,
    quantity: Int,
    onQuantitySelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Text(
            text = title,
            color = PrimaryGreen,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$price EGP",
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Row قابل للضغط بدون ripple
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFECECEC), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp)
                .clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val ticketText = if (quantity == 1) "Ticket" else "Tickets"
            Text("$quantity $ticketText")
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            (0..10).forEach { num ->
                DropdownMenuItem(
                    text = { Text("$num") },
                    onClick = {
                        onQuantitySelected(num)
                        expanded = false
                    }
                )
            }
        }
    }
}

