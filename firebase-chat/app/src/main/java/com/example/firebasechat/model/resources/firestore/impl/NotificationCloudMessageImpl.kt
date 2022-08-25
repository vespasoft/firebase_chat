package com.example.firebasechat.model.resources.firestore.impl

import com.example.firebasechat.model.PushNotification
import com.example.firebasechat.model.resources.firestore.NotificationCloudMessage
import com.example.firebasechat.model.resources.remote.SendNotificationRemote
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import javax.inject.Inject

class NotificationCloudMessageImpl @Inject constructor(
    private val sendNotificationRemote: SendNotificationRemote,
    private val firebaseMessaging: FirebaseMessaging
): NotificationCloudMessage {
    override fun fetchingFCMRegistrationToken(onRegistrationToken: (String) -> Unit) {
        firebaseMessaging.token
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

    override suspend fun sendNotification(param: PushNotification): Int {
        val response = sendNotificationRemote.invoke(param = param)
        return response.status.value
    }
}