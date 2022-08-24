package com.example.firebasechat.model.repository.impl

import com.example.firebasechat.model.NotificationRequest
import com.example.firebasechat.model.repository.NotificationRepository
import com.example.firebasechat.model.resources.remote.clients.SendNotificationHttpClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor() : NotificationRepository {
    private val sendNotificationHttpClient = SendNotificationHttpClient()

    override fun fetchingFCMRegistrationToken(onRegistrationToken: (String) -> Unit) {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }

                    // Get new FCM registration token
                    onRegistrationToken(task.result)
                }
            )
    }

    override suspend fun sendNotification(param: NotificationRequest): Int {
        val response = sendNotificationHttpClient.invoke(param = param)
        return response.status.value
    }
}