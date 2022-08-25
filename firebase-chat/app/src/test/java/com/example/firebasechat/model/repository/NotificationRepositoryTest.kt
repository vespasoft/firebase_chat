package com.example.firebasechat.model.repository

import com.example.firebasechat.model.PushNotification
import com.example.firebasechat.model.repository.impl.NotificationRepositoryImpl
import com.example.firebasechat.model.resources.firestore.NotificationCloudMessage
import com.example.firebasechat.model.resources.remote.FcmRequest
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class NotificationRepositoryTest {
    private val notificationCloudMessage = mockk<NotificationCloudMessage>(relaxed = true)
    private var notificationRepository = NotificationRepositoryImpl(notificationCloudMessage)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Before
    fun after() {
        clearAllMocks()
    }

    @Test
    fun `when send notification is OK then result should be 200`() = runBlocking {
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
    fun `when send notification gives an exception then result should be 500`() = runBlocking {
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