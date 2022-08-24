package com.example.firebasechat.model

import com.example.firebasechat.model.repository.impl.NotificationRepositoryImpl
import com.example.firebasechat.model.resources.firestore.NotificationCloudMessage
import com.example.firebasechat.model.resources.remote.FcmRequest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test

class NotificationRepositoryTest {
    private val notificationCloudMessage = mockk<NotificationCloudMessage>(relaxed = true)

    private var notificationRepository = NotificationRepositoryImpl(notificationCloudMessage)

    @Test
    fun `When send notification is OK THEN result shouldBe 200`() = runBlocking {
        val pushNotification = PushNotification(
            fcmData = FcmRequest(title = "title", body = "body"),
            token = "token",
            topic = "topic"
        )
        coEvery { notificationCloudMessage.sendNotification(any()) } returns 200

        val expected = 200
        val given = notificationRepository.sendNotification(pushNotification)

        assert(given == expected)
    }

    @Test
    fun `When send notification gives an exception THEN result shouldBe 500`() = runBlocking {
        val pushNotification = PushNotification(
            fcmData = FcmRequest(title = "title", body = "body"),
            token = "token",
            topic = "topic"
        )
        coEvery { notificationCloudMessage.sendNotification(any()) } returns 500

        val expected = 500
        val given = notificationRepository.sendNotification(pushNotification)

        assert(given == expected)
    }
}