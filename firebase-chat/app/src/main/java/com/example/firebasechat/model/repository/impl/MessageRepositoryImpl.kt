package com.example.firebasechat.model.repository.impl

import com.example.firebasechat.model.Message
import com.example.firebasechat.model.repository.MessageRepository
import com.example.firebasechat.model.resources.firestore.MessageFirestoreDatabase
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageFirestoreDatabase: MessageFirestoreDatabase
): MessageRepository {
    override fun addListener(
        addresseeId: String,
        senderId: String,
        onMessageEvent: (Boolean, Message) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        messageFirestoreDatabase.addListener(addresseeId, senderId, onMessageEvent, onError)
    }

    override fun removeListener() {
        messageFirestoreDatabase.removeListener()
    }

    override fun saveMessage(msg: Message, onResult: (Throwable?) -> Unit) {
        messageFirestoreDatabase.saveMessage(msg, onResult)
    }
}