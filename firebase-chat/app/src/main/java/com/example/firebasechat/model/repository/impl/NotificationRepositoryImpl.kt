package com.example.firebasechat.model.repository.impl

import com.example.firebasechat.model.PushNotification
import com.example.firebasechat.model.repository.NotificationRepository
import com.example.firebasechat.model.resources.firestore.NotificationCloudMessage
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationCloudMessage: NotificationCloudMessage
) : NotificationRepository {

    override fun fetchingFCMRegistrationToken(onRegistrationToken: (String) -> Unit) {
        notificationCloudMessage.fetchingFCMRegistrationToken {
            onRegistrationToken(it)
        }
    }

    override suspend fun sendNotification(param: PushNotification): Int {
        return notificationCloudMessage.sendNotification(param)
    }
}