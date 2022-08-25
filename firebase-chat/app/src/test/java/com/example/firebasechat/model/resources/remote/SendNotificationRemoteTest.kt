package com.example.firebasechat.model.resources.remote

import com.example.firebasechat.model.PushNotification
import com.example.firebasechat.model.resources.remote.impl.SendNotificationRemoteImpl
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.Test

class SendNotificationRemoteTest {

    private val httpClient = mockk<HttpClient>(relaxed = true)
    private var sendNotificationRemote = SendNotificationRemoteImpl(httpClient)

    @Test
    fun `When invoke is OK THEN result should Be 200`() =
        runTest {
            val firebaseAPIUrl = "https://fcm.googleapis.com/fcm/send"
            val pushNotification = PushNotification(
                fcmData = FcmRequest(title = "title", body = "body"),
                token = "token",
                topic = "topic"
            )
            coEvery { httpClient.post(
                HttpRequestBuilder().apply {
                    url(firebaseAPIUrl)
                    headers.append("Authorization", Header.key)
                    headers.append("Content-Type", Header.contentType)
                    contentType(ContentType.Application.Json)
                    setBody(
                        FcmBody(
                            to = "token",
                            notification = FcmRequest(title = "title", body = "body")
                        )
                    )
                }
            ) } returns mockk(relaxed = true) {
                every { status } returns mockk(relaxed = true) {
                    every { value } returns 200
                }
            }

            val expected = 200
            val given = sendNotificationRemote.invoke(pushNotification)

            assert(given.status.value == expected)
    }

}