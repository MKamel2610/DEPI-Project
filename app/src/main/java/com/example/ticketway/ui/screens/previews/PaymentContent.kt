package com.example.ticketway.ui.screens.previews

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentScreenContent(
    totalPrice: Int,
    cardNumber: String,
    expirationDate: String,
    cvv: String,
    cardholderName: String,
    localError: String?,
    isLoading: Boolean,
    onCardNumberChange: (String) -> Unit,
    onExpirationDateChange: (String) -> Unit,
    onCvvChange: (String) -> Unit,
    onCardholderNameChange: (String) -> Unit,
    onClose: () -> Unit,
    onPayNow: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Payment Gateway",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close Payment",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "PAY EGP ${totalPrice}.00",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Card Number
            OutlinedTextField(
                value = cardNumber,
                onValueChange = onCardNumberChange,
                label = { Text("Card Number") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Expiry Date
                OutlinedTextField(
                    value = expirationDate,
                    onValueChange = onExpirationDateChange,
                    label = { Text("MM/YY") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Right) }
                    ),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
                // CVV
                OutlinedTextField(
                    value = cvv,
                    onValueChange = onCvvChange,
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Cardholder Name
            OutlinedTextField(
                value = cardholderName,
                onValueChange = onCardholderNameChange,
                label = { Text("Cardholder Name") },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            // Error Display
            if (localError != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(localError, color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Pay Button
            Button(
                onClick = onPayNow,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Pay Now", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ==================== Previews ====================

@Preview(name = "Payment Screen - Empty", showBackground = true)
@Composable
private fun PreviewPaymentScreenEmpty() {
    MaterialTheme {
        PaymentScreenContent(
            totalPrice = 400,
            cardNumber = "",
            expirationDate = "",
            cvv = "",
            cardholderName = "",
            localError = null,
            isLoading = false,
            onCardNumberChange = {},
            onExpirationDateChange = {},
            onCvvChange = {},
            onCardholderNameChange = {},
            onClose = {},
            onPayNow = {}
        )
    }
}

@Preview(name = "Payment Screen - Partially Filled", showBackground = true)
@Composable
private fun PreviewPaymentScreenPartial() {
    MaterialTheme {
        PaymentScreenContent(
            totalPrice = 600,
            cardNumber = "1234567890",
            expirationDate = "12/25",
            cvv = "",
            cardholderName = "",
            localError = null,
            isLoading = false,
            onCardNumberChange = {},
            onExpirationDateChange = {},
            onCvvChange = {},
            onCardholderNameChange = {},
            onClose = {},
            onPayNow = {}
        )
    }
}

@Preview(name = "Payment Screen - Complete", showBackground = true)
@Composable
private fun PreviewPaymentScreenComplete() {
    MaterialTheme {
        PaymentScreenContent(
            totalPrice = 800,
            cardNumber = "1234567890123456",
            expirationDate = "12/25",
            cvv = "123",
            cardholderName = "John Doe",
            localError = null,
            isLoading = false,
            onCardNumberChange = {},
            onExpirationDateChange = {},
            onCvvChange = {},
            onCardholderNameChange = {},
            onClose = {},
            onPayNow = {}
        )
    }
}

@Preview(name = "Payment Screen - With Error", showBackground = true)
@Composable
private fun PreviewPaymentScreenError() {
    MaterialTheme {
        PaymentScreenContent(
            totalPrice = 400,
            cardNumber = "12345678",
            expirationDate = "12/25",
            cvv = "123",
            cardholderName = "John Doe",
            localError = "Please enter a valid 16-digit card number.",
            isLoading = false,
            onCardNumberChange = {},
            onExpirationDateChange = {},
            onCvvChange = {},
            onCardholderNameChange = {},
            onClose = {},
            onPayNow = {}
        )
    }
}

@Preview(name = "Payment Screen - Loading", showBackground = true)
@Composable
private fun PreviewPaymentScreenLoading() {
    MaterialTheme {
        PaymentScreenContent(
            totalPrice = 800,
            cardNumber = "1234567890123456",
            expirationDate = "12/25",
            cvv = "123",
            cardholderName = "John Doe",
            localError = null,
            isLoading = true,
            onCardNumberChange = {},
            onExpirationDateChange = {},
            onCvvChange = {},
            onCardholderNameChange = {},
            onClose = {},
            onPayNow = {}
        )
    }
}