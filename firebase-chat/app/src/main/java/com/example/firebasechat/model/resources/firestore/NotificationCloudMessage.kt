package com.example.firebasechat.model.resources.firestore

import com.example.firebasechat.model.PushNotification

interface NotificationCloudMessage {
    fun fetchingFCMRegistrationToken(onRegistrationToken: (String) -> Unit)
    suspend fun sendNotification(param: PushNotification): Int
}