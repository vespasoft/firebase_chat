package com.example.firebasechat.model.resources.remote

@kotlinx.serialization.Serializable
data class FcmRequest(
    val title: String,
    val body: String
)