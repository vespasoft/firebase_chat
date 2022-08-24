package com.example.firebasechat.model.resources.remote.clients

import com.example.firebasechat.model.NotificationRequest
import com.example.firebasechat.model.resources.remote.BaseHttpClient
import com.example.firebasechat.model.resources.remote.FcmBody
import com.example.firebasechat.model.resources.remote.Header
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SendNotificationHttpClient: BaseHttpClient<NotificationRequest, HttpResponse>() {
    override val serverUrl: String
        get() = "https://fcm.googleapis.com/fcm/send"

    override suspend fun invoke(param: NotificationRequest): HttpResponse = kotlin.run {
        withContext(Dispatchers.Default) {
            client.post(
                HttpRequestBuilder().apply {
                    url(serverUrl)
                    headers.append("Authorization", Header.key)
                    headers.append("Content-Type", Header.contentType)
                    contentType(ContentType.Application.Json)
                    setBody(
                        FcmBody(
                            to = param.token ?: param.topic,
                            notification = param.fcmData
                        )
                    )
                }
            )
        }
    }
}

