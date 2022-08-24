package com.example.firebasechat.model.repository

import com.example.firebasechat.model.PushNotification

interface NotificationRepository {
    fun fetchingFCMRegistrationToken(onRegistrationToken: (String) -> Unit)
    suspend fun sendNotification(param: PushNotification): Int
}