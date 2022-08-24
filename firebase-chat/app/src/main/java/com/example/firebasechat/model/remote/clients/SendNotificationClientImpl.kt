package com.example.firebasechat.model.remote.clients

import com.example.firebasechat.model.PushNotification
import com.example.firebasechat.model.remote.FcmBody
import com.example.firebasechat.model.remote.Header
import com.example.firebasechat.model.remote.SendNotificationClient
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class SendNotificationClientImpl @Inject constructor(): SendNotificationClient {
    override val client: HttpClient
        get() = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }

    override val serverUrl: String
        get() = "https://fcm.googleapis.com/fcm/send"

    override suspend fun invoke(param: PushNotification): HttpResponse = kotlin.run {
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

