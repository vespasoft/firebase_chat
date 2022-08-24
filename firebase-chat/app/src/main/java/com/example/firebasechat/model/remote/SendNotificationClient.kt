package com.example.firebasechat.model.remote

import com.example.firebasechat.model.PushNotification
import io.ktor.client.*
import io.ktor.client.statement.*

interface SendNotificationClient {
    val client: HttpClient
    val serverUrl: String
    suspend fun invoke(param: PushNotification): HttpResponse
}