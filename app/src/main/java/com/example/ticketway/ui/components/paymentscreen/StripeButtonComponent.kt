package com.example.ticketway.ui.components.paymentscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun StripeButtonComponent(
    amountInCents: Int,
    onStartPayment: (amountInCents: Int) -> Unit
) {
    var loading by remember { mutableStateOf(false) }

    Button(
        onClick = {
            loading = true
            onStartPayment(amountInCents)
            loading = false
        },
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF009688))
    ) {
        if (loading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(20.dp))
        } else {
            Text("Pay with Stripe", color = Color.White)
        }
    }
}

