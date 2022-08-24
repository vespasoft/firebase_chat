package com.example.firebasechat.model

import com.example.firebasechat.model.resources.remote.FcmRequest

@kotlinx.serialization.Serializable
class NotificationRequest(
    val fcmData: FcmRequest,
    val token: String?,
    val topic: String = "default"
)

