package com.example.firebasechat.model.resources.firestore

import com.example.firebasechat.model.Message

interface MessageFirestoreDatabase {
    fun addListener(
        addresseeId: String,
        senderId: String,
        onMessageEvent: (Boolean, Message) -> Unit,
        onError: (Throwable) -> Unit
    )
    fun removeListener()
    fun saveMessage(msg: Message, onResult: (Throwable?) -> Unit)
}