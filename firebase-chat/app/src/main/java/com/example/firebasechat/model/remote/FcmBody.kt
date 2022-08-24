package com.example.firebasechat.model.remote

@kotlinx.serialization.Serializable
data class FcmBody(
    val to: String,
    val notification: FcmRequest
)