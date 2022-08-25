package com.example.firebasechat.model.resources.firestore

import com.example.firebasechat.model.PushNotification
import com.example.firebasechat.model.resources.firestore.impl.NotificationCloudMessageImpl
import com.example.firebasechat.model.resources.remote.FcmRequest
import com.example.firebasechat.model.resources.remote.SendNotificationRemote
import com.google.firebase.messaging.FirebaseMessaging
import io.ktor.client.statement.*
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class NotificationCloudMessageTest {
    private val sendNotificationRemote = mockk<SendNotificationRemote>(relaxed = true)
    private val firebaseMessaging = mockk<FirebaseMessaging>(relaxed = true)

    private var notificationCloudMessage =
        NotificationCloudMessageImpl(sendNotificationRemote, firebaseMessaging)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Before
    fun after() {
        clearAllMocks()
    }

    @Test
    fun `When invoke is OK THEN result should Be 200`() = runBlocking {
        val pushNotification = PushNotification(
            fcmData = FcmRequest(title = "title", body = "body"),
            token = "token",
            topic = "topic"
        )
        coEvery { sendNotificationRemote.invoke(pushNotification) } returns mockk<HttpResponse>(
            relaxed = true
        ) {
            every { status } returns mockk(relaxed = true) {
                every { value } returns 200
            }
        }

        val expected = 200
        val given = notificationCloudMessage.sendNotification(pushNotification)

        assert(given == expected)
    }

    @Test
    fun `When fetchingFCMRegistrationToken is OK THEN result should Be the FCM token`() {
        val expected = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        coEvery { firebaseMessaging.token } returns mockk(relaxed = true) {
            every { result } returns expected
            every { isSuccessful } returns true
        }

        notificationCloudMessage.fetchingFCMRegistrationToken { given ->
            assert(given == expected)
        }
    }

}