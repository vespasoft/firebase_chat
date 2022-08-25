package com.example.firebasechat.model.resources.remote.impl

import com.example.firebasechat.model.PushNotification
import com.example.firebasechat.model.resources.remote.FcmBody
import com.example.firebasechat.model.resources.remote.Header
import com.example.firebasechat.model.resources.remote.SendNotificationRemote
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendNotificationRemoteImpl @Inject constructor(
    private val client: HttpClient
): SendNotificationRemote {
    override val firebaseAPIUrl: String
        get() = "https://fcm.googleapis.com/fcm/send"

    override suspend fun invoke(param: PushNotification): HttpResponse = kotlin.run {
        withContext(Dispatchers.Default) {
            client.post(
                HttpRequestBuilder().apply {
                    url(firebaseAPIUrl)
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

