package com.example.firebasechat.model.resources.remote

import com.example.firebasechat.model.PushNotification
import io.ktor.client.*
import io.ktor.client.statement.*

interface SendNotificationRemote {
    val firebaseAPIUrl: String
    suspend fun invoke(param: PushNotification): HttpResponse
}