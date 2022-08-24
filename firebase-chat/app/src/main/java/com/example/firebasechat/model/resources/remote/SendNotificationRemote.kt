package com.example.firebasechat.model.resources.remote

import com.example.firebasechat.model.PushNotification
import io.ktor.client.*
import io.ktor.client.statement.*

interface SendNotificationRemote {
    val client: HttpClient
    val serverUrl: String
    suspend fun invoke(param: PushNotification): HttpResponse
}