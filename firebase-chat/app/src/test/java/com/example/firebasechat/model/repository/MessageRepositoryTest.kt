package com.example.firebasechat.model.repository

import com.example.firebasechat.model.Message
import com.example.firebasechat.model.PushNotification
import com.example.firebasechat.model.repository.impl.MessageRepositoryImpl
import com.example.firebasechat.model.resources.firestore.MessageFirestoreDatabase
import com.example.firebasechat.model.resources.remote.FcmRequest
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class MessageRepositoryTest {
    private val messageFirestoreDatabase = mockk<MessageFirestoreDatabase>(relaxed = true)
    private var messageRepository = MessageRepositoryImpl(messageFirestoreDatabase)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Before
    fun after() {
        clearAllMocks()
    }

    @Test
    fun `when save message success throwable should be null`() = runBlocking {
        val expected = null
        val msg = Message(
            id = "id",
            senderId = "123",
            addresseeId = "123",
            author = "Carlos",
            content = "content",
            timestamp = "0"
        )

        every { messageFirestoreDatabase.saveMessage(any(), any()) } answers {
            secondArg<(Throwable?) -> Unit>().invoke(null)
        }

        messageRepository.saveMessage(msg) { throwable ->
            assert(throwable == expected)
        }
    }

    @Test
    fun `when save message fails throwable should not be null`() = runBlocking {
        val expected = "error unexpectedly"
        val msg = Message(
            id = "id",
            senderId = "123",
            addresseeId = "123",
            author = "Carlos",
            content = "content",
            timestamp = "0"
        )

        every { messageFirestoreDatabase.saveMessage(any(), any()) } answers {
            secondArg<(Throwable?) -> Unit>().invoke(Throwable("error unexpectedly"))
        }

        messageRepository.saveMessage(msg) { throwable ->
            assert(throwable?.message == expected)
        }
    }
}