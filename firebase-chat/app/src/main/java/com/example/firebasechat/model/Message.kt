package com.example.firebasechat.model

data class Message(
    val id: String = "",
    val senderId: String = "",
    val addresseeId: String = "",
    val author: String = "",
    val content: String? = null,
    val timestamp: String = "",
    val image: String? = null
)