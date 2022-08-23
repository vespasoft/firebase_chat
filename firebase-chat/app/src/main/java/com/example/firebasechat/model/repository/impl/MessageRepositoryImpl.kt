package com.example.firebasechat.model.repository.impl

import com.example.firebasechat.model.Message
import com.example.firebasechat.model.repository.MessageRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(): MessageRepository {

    private var listenerRegistration: ListenerRegistration? = null

    override fun addListener(
        senderId: String,
        addresseeId: String,
        onMessageEvent: (Boolean, Message) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val firstQuery = Firebase.firestore.collection(MESSAGE_COLLECTION)
            .whereEqualTo(SENDER_ID, senderId)
            .whereEqualTo(ADDRESSEE_ID, addresseeId)

        listenerRegistration = firstQuery.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            value?.documentChanges?.forEach {
                val wasMessageDeleted = it.type == DocumentChange.Type.REMOVED
                val message = it.document.toObject<Message>().copy(id = it.document.id)
                onMessageEvent(wasMessageDeleted, message)
            }
        }
    }

    override fun removeListener() {
        listenerRegistration?.remove()
    }

    override fun saveMessage(msg: Message, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(MESSAGE_COLLECTION)
            .add(msg)
            .addOnCompleteListener { onResult(it.exception) }
    }

    companion object {
        private const val MESSAGE_COLLECTION = "Messages"
        private const val SENDER_ID = "senderId"
        private const val ADDRESSEE_ID = "addresseeId"
    }
}