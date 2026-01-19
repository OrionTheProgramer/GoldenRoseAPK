package com.example.golden_rose_apk.model

import androidx.compose.ui.graphics.vector.ImageVector


data class LoginResponse(
    val token: String
)

data class BottomNavItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

data class Category(
    val name: String,
    val color: androidx.compose.ui.graphics.Color
)

data class ProductFirestore(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val type: String = "",
    val category: String = "",
    val image: String = "",
    val desc: String = ""
)

data class CheckoutItem(
    val name: String,
    val quantity: Int,
    val price: Double
)

data class BlogPost(
    val id: Int,
    val title: String,
    val author: String,
    val date: String,
    val preview: String,
    val likes: Int = 0,
    val content: String,
    val comments: Int = 0
)

data class UserUi(
    val displayName: String = "",
    val nickname: String = "",
    val email: String = "",
    val country: String = "",
    val darkMode: Boolean = false
)

