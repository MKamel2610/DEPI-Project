//package com.example.ticketway.ui.preview
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material3.*
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.example.ticketway.R
//import com.example.ticketway.data.model.BookingItem
//import com.example.ticketway.ui.components.tierselectionscreen.TierOptionCard
//import java.util.UUID
//
//// ================== Colors ==================
//private val PrimaryGreen = Color(0xFF009688)
//private val DarkText = Color(0xFF222222)
//private val MutedGray = Color(0xFF9E9E9E)
//private val CardBg = Color(0xFFF5F5F5)
//private val AccentRed = Color(0xFFE53935)
//
//// ================== Preview Controller ==================
//class PreviewBookingController {
//    var tempBooking by mutableStateOf<BookingItem?>(null)
//
//    var regularCount by mutableStateOf(0)
//    var premiumCount by mutableStateOf(0)
//    var vipCount by mutableStateOf(0)
//
//    fun updateRegular(v: Int) { regularCount = v }
//    fun updatePremium(v: Int) { premiumCount = v }
//    fun updateVip(v: Int) { vipCount = v }
//
//    fun getTotalPrice(): Int = (regularCount * 100) + (premiumCount * 200) + (vipCount * 300)
//
//    fun saveBooking(
//        fixtureId: Int,
//        homeTeam: String,
//        awayTeam: String,
//        stadiumName: String,
//        matchDate: String
//    ) {
//        tempBooking = BookingItem(
//            bookingId = UUID.randomUUID().toString(),
//            fixtureId = fixtureId,
//            userId = "preview-user",
//            homeTeam = homeTeam,
//            awayTeam = awayTeam,
//            stadiumName = stadiumName,
//            matchDate = matchDate,
//            regularCount = regularCount,
//            premiumCount = premiumCount,
//            vipCount = vipCount,
//            seatCount = regularCount + premiumCount + vipCount,
//            totalPrice = getTotalPrice(),
//            paymentStatus = "PENDING"
//        )
//        // reset selection after save
//        regularCount = 0
//        premiumCount = 0
//        vipCount = 0
//    }
//
//    fun clearTempBooking() { tempBooking = null }
//}
//
//// ================== TierOptionCard Component ==================
//@Composable
//fun TierOptionCard(
//    title: String,
//    price: Int,
//    quantity: Int,
//    onQuantitySelected: (Int) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(14.dp),
//        colors = CardDefaults.cardColors(containerColor = Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceBetween
//        ) {
//            Column {
//                Text(title, color = PrimaryGreen, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
//                Spacer(modifier = Modifier.height(6.dp))
//                Text("$price EGP", color = MutedGray, style = MaterialTheme.typography.bodyMedium)
//            }
//
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Button(
//                    onClick = { if (quantity > 0) onQuantitySelected(quantity -1) },
//                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF7F7F7)),
//                    shape = RoundedCornerShape(8.dp),
//                    modifier = Modifier.size(width = 40.dp, height = 36.dp)
//                ) { Text("-", color = DarkText) }
//
//                Spacer(modifier = Modifier.width(10.dp))
//                Text("$quantity", style = MaterialTheme.typography.titleMedium, color = DarkText)
//                Spacer(modifier = Modifier.width(10.dp))
//
//                Button(
//                    onClick = { onQuantitySelected(quantity +1) },
//                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
//                    shape = RoundedCornerShape(8.dp),
//                    modifier = Modifier.size(width = 40.dp, height = 36.dp)
//                ) { Text("+", color = Color.White) }
//            }
//        }
//    }
//}
//
//// ================== ConfirmCancelBar ==================
//@Composable
//fun ConfirmCancelBar(
//    totalPrice: Int,
//    onConfirm: () -> Unit,
//    onCancel: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier.fillMaxWidth().padding(12.dp),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Button(
//            onClick = onCancel,
//            colors = ButtonDefaults.buttonColors(containerColor = AccentRed),
//            shape = RoundedCornerShape(12.dp),
//            modifier = Modifier.weight(0.42f)
//        ) { Text("Cancel", color = Color.White) }
//
//        Spacer(modifier = Modifier.width(12.dp))
//
//        Button(
//            onClick = onConfirm,
//            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
//            shape = RoundedCornerShape(12.dp),
//            modifier = Modifier.weight(0.58f)
//        ) {
//            Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                Text("Confirm", color = Color.White, style = MaterialTheme.typography.titleMedium)
//                Text("• $totalPrice EGP", color = Color.White, style = MaterialTheme.typography.bodySmall)
//            }
//        }
//    }
//}
//
//// ================== Tier Selection Preview ==================
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TierSelectionPreviewScreenFixed(
//    controller: PreviewBookingController,
//    fixtureId: Int,
//    homeTeam: String,
//    awayTeam: String,
//    stadiumName: String,
//    matchDate: String,
//    onNavigateToSummary: () -> Unit
//) {
//    var reg by remember { mutableStateOf(controller.regularCount) }
//    var prem by remember { mutableStateOf(controller.premiumCount) }
//    var vip by remember { mutableStateOf(controller.vipCount) }
//
//    val total = (reg*100) + (prem*200) + (vip*300)
//
//    LaunchedEffect(reg, prem, vip) {
//        controller.updateRegular(reg)
//        controller.updatePremium(prem)
//        controller.updateVip(vip)
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Select Tier", color = DarkText) },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        controller.clearTempBooking()
//                        reg = 0; prem =0; vip=0
//                    }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkText)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
//            )
//        }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
//                    .padding(16.dp)
//            ) {
//                TierOptionCard("Regular", 100, reg) { reg = it }
//                Spacer(modifier = Modifier.height(8.dp))
//                TierOptionCard("Premium", 200, prem) { prem = it }
//                Spacer(modifier = Modifier.height(8.dp))
//                TierOptionCard("VIP", 300, vip) { vip = it }
//                Spacer(modifier = Modifier.height(80.dp)) // مساحة للزرار تظهر فوقها
//            }
//
//            if (total > 0) {
//                ConfirmCancelBar(
//                    totalPrice = total,
//                    onCancel = {
//                        reg=0; prem=0; vip=0
//                        controller.clearTempBooking()
//                    },
//                    onConfirm = {
//                        controller.saveBooking(fixtureId, homeTeam, awayTeam, stadiumName, matchDate)
//                        onNavigateToSummary()
//                    },
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(16.dp)
//                )
//            }
//        }
//    }
//}
//
//
//// ================== Booking Summary Preview ==================
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BookingSummaryPreviewScreen(
//    controller: PreviewBookingController,
//    onBack: () -> Unit,
//    onContinuePayment: (BookingItem) -> Unit
//) {
//    val booking = controller.tempBooking
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Booking Summary", color = DarkText) },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        controller.clearTempBooking()
//                        onBack()
//                    }) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = DarkText)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//                .verticalScroll(rememberScrollState())
//                .padding(16.dp)
//                .background(Color.White),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            if (booking == null) {
//                Spacer(modifier = Modifier.height(60.dp))
//                Text("no bookings right now", color = MutedGray, style = MaterialTheme.typography.bodyLarge)
//            } else {
//                Card(
//                    modifier = Modifier.fillMaxWidth(0.9f).heightIn(min=180.dp).padding(8.dp),
//                    shape = RoundedCornerShape(16.dp),
//                    colors = CardDefaults.cardColors(containerColor = CardBg),
//                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//                ) {
//                    Column(
//                        modifier = Modifier.fillMaxSize().padding(20.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally
//                    ) {
//                        Text("${booking.homeTeam} vs ${booking.awayTeam}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = DarkText)
//                        Spacer(modifier = Modifier.height(8.dp))
//                        Text("${booking.stadiumName} • ${booking.matchDate}", style = MaterialTheme.typography.bodyLarge, color = MutedGray)
//                        Spacer(modifier = Modifier.height(12.dp))
//                        if (booking.regularCount > 0) Text("Regular: ${booking.regularCount} x 100 EGP", color = DarkText)
//                        if (booking.premiumCount > 0) Text("Premium: ${booking.premiumCount} x 200 EGP", color = DarkText)
//                        if (booking.vipCount > 0) Text("VIP: ${booking.vipCount} x 300 EGP", color = DarkText)
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Text("Total: ${booking.totalPrice} EGP", style = MaterialTheme.typography.titleMedium, color = PrimaryGreen)
//                        Spacer(modifier = Modifier.height(16.dp))
//                        Button(
//                            onClick = { onContinuePayment(booking) },
//                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
//                            modifier = Modifier.fillMaxWidth(0.7f),
//                            shape = RoundedCornerShape(12.dp)
//                        ) { Text("Continue to Payment", color = Color.White) }
//                    }
//                }
//            }
//        }
//    }
//}
//
//// ================== Payment Preview ==================
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PaymentPreviewScreen(
//    controller: PreviewBookingController,
//    onBack: () -> Unit
//) {
//    val booking = controller.tempBooking
//    val totalPrice = booking?.totalPrice ?: 0
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Payment", color = Color.Black) },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.Black)
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
//            )
//        }
//    ) { padding ->
//        Column(
//            modifier = Modifier
//                .padding(padding)
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//            // ----- Review Card -----
//            Card(
//                shape = RoundedCornerShape(18.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F1F1)),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Column(modifier = Modifier.padding(18.dp)) {
//                    Text(
//                        text = "Booking Review",
//                        style = MaterialTheme.typography.titleMedium,
//                        color = PrimaryGreen,
//                        fontWeight = FontWeight.Bold
//                    )
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Text("Teams: ${booking?.homeTeam} vs ${booking?.awayTeam}", color = Color.Black)
//                    Text("Stadium: ${booking?.stadiumName}", color = Color.Black)
//                    Text("Match Date: ${booking?.matchDate}", color = Color.Black)
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Text(
//                        text = "Total Price : $totalPrice EGP",
//                        fontWeight = FontWeight.Bold,
//                        color = PrimaryGreen,
//                        style = MaterialTheme.typography.titleLarge
//                    )
//                }
//            }
//
//            // ----- Stripe Card -----
//            Card(
//                shape = RoundedCornerShape(20.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
//                colors = CardDefaults.cardColors(containerColor = Color(0xFF635BFF)),
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(20.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Stripe",
//                        color = Color.White,
//                        fontWeight = FontWeight.Bold,
//                        style = MaterialTheme.typography.headlineMedium
//                    )
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Button(
//                        onClick = { /* Do nothing in preview */ },
//                        modifier = Modifier.fillMaxWidth().height(50.dp),
//                        shape = RoundedCornerShape(12.dp),
//                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
//                    ) {
//                        Text(
//                            "Continue with Stripe",
//                            color = Color(0xFF635BFF),
//                            fontWeight = FontWeight.Bold
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//// ================== Full Flow Three Screens Preview ==================
//@Preview(showBackground = true)
//@Composable
//fun FullFlowThreeScreensPreview() {
//    val controller = remember { PreviewBookingController() }
//    var screen by remember { mutableStateOf("tier") }
//
//    MaterialTheme {
//        when(screen) {
//            "tier" -> TierSelectionPreviewScreenFixed(
//                controller=controller,
//                fixtureId=1,
//                homeTeam="Al Ahly",
//                awayTeam="Zamalek",
//                stadiumName="Cairo Stadium",
//                matchDate="22-11-2025"
//            ) { screen="summary" }
//
//            "summary" -> BookingSummaryPreviewScreen(
//                controller=controller,
//                onBack={ screen="tier" },
//                onContinuePayment={ _ -> screen="payment" }
//            )
//
//            "payment" -> PaymentPreviewScreen(
//                controller=controller,
//                onBack={ screen="summary" }
//            )
//        }
//    }
//}
//
//
