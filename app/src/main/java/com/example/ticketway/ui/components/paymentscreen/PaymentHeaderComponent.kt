package com.example.ticketway.ui.components.paymentscreen

import androidx.compose.foundation.layout.height
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentHeaderComponent(
    title: String,
    onBackClick: () -> Unit
) {
    TopAppBar(
        title = { Text(title, color = Color.Black) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
        modifier = androidx.compose.ui.Modifier
            .height(56.dp)
    )
}

