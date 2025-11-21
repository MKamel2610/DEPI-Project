package com.example.ticketway.ui.components.homescreen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomNavigationBar(
    selectedTab: String = "home",
    onTabSelected: (String) -> Unit = {}
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedTab == "home",
            onClick = { onTabSelected("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedTab == "My Tickets",
            onClick = { onTabSelected("My Tickets") },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "My Tickets") },
            label = { Text("My Tickets", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
                indicatorColor = Color.Transparent
            )
        )

//        NavigationBarItem(
//            selected = selectedTab == "watch",
//            onClick = { onTabSelected("watch") },
//            icon = { Icon(Icons.Default.PlayArrow, contentDescription = "Watch") },
//            label = { Text("Watch", fontSize = 11.sp) },
//            colors = NavigationBarItemDefaults.colors(
//                selectedIconColor = PrimaryGreen,
//                selectedTextColor = PrimaryGreen,
//                indicatorColor = Color.Transparent
//            )
//        )

//        NavigationBarItem(
//            selected = selectedTab == "news",
//            onClick = { onTabSelected("news") },
//            icon = { Icon(Icons.Default.List, contentDescription = "News") },
//            label = { Text("News", fontSize = 11.sp) },
//            colors = NavigationBarItemDefaults.colors(
//                selectedIconColor = PrimaryGreen,
//                selectedTextColor = PrimaryGreen,
//                indicatorColor = Color.Transparent
//            )
//        )

        NavigationBarItem(
            selected = selectedTab == "account",
            onClick = { onTabSelected("account") },
            icon = { Icon(Icons.Default.Person, contentDescription = "Account") },
            label = { Text("Account", fontSize = 11.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryGreen,
                selectedTextColor = PrimaryGreen,
                indicatorColor = Color.Transparent
            )
        )
    }
}