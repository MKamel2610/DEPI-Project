package com.example.ticketway.ui.components.tierselectionscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketway.ui.ui.theme.DarkText
import com.example.ticketway.ui.ui.theme.PrimaryGreen
import com.example.ticketway.ui.ui.theme.LightText

@Composable
fun TierOptionCard(
    title: String,
    price: Int,
    quantity: Int,
    onQuantityChange: (Int) -> Unit,
    canIncrease: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        // Visual cue if tickets are selected in this tier
        border = BorderStroke(1.dp, if (quantity > 0) PrimaryGreen else MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Tier Info
            Column {
                Text(
                    text = "$title Tier",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )
                Text(
                    text = "EGP $price",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = PrimaryGreen
                )
            }

            // Selector Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Decrease Button
                IconButton(
                    onClick = { onQuantityChange(quantity - 1) },
                    enabled = quantity > 0,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = PrimaryGreen,
                        disabledContentColor = LightText
                    )
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Decrease")
                }

                // Count Text
                Text(
                    text = quantity.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkText
                )

                // Increase Button
                IconButton(
                    onClick = { onQuantityChange(quantity + 1) },
                    enabled = canIncrease,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = PrimaryGreen,
                        disabledContentColor = LightText
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Increase")
                }
            }
        }
    }
}