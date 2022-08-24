package com.example.firebasechat.model.repository

import com.example.firebasechat.model.NotificationRequest

interface NotificationRepository {
    fun fetchingFCMRegistrationToken(onRegistrationToken: (String) -> Unit)
    suspend fun sendNotification(param: NotificationRequest): Int
}