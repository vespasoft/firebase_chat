package com.example.firebasechat.ui.screens.chat

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.firebasechat.USER_DEFAULT_ID
import com.example.firebasechat.model.Message
import com.example.firebasechat.model.NotificationRequest
import com.example.firebasechat.model.User
import com.example.firebasechat.model.repository.*
import com.example.firebasechat.model.resources.remote.FcmRequest
import com.example.firebasechat.ui.common.ext.idFromParameter
import com.example.firebasechat.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    logRepository: LogRepository,
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository,
    private val messageRepository: MessageRepository,
    private val notificationRepository: NotificationRepository
): BaseViewModel(logRepository) {
    var uiState = mutableStateOf(ChatUiState())
        private set

    var loggedUser = mutableStateOf(User())

    var messages = mutableStateMapOf<String, Message>()
        private set

    fun initialize(userId: String) {
        viewModelScope.launch(showErrorExceptionHandler) {
            if (userId != USER_DEFAULT_ID) {
                userRepository.getUser(userId.idFromParameter(), ::onError) { userSelected ->
                    uiState.value = ChatUiState(
                        chatTitle = userSelected.name,
                        userSelected = userSelected
                    )
                    addMessageListener(userSelected.userId, loggedUser.value.userId)
                    addMessageListener(loggedUser.value.userId, userSelected.userId)
                }
            }
        }
    }

    fun addLoggedUserListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            userRepository.addLoggedUserListener(accountRepository.getUserId(), ::onLoggedUserEvent, ::onError)
        }
    }

    fun sendMessage(msg: Message) {
        viewModelScope.launch(showErrorExceptionHandler) {

            messageRepository.saveMessage(
                msg.copy(
                    addresseeId = uiState.value.userSelected.userId,
                    senderId = loggedUser.value.userId,
                    author = loggedUser.value.name,
                    timestamp = System.currentTimeMillis().toString()
                )
            ) { error ->
                if (error != null) {
                    onError(error)
                }

                if (error == null) {
                    sendPushNotification(
                        title = loggedUser.value.name,
                        body = msg.content.toString(),
                        fcmToken = uiState.value.userSelected.fcmToken
                    )
                }
            }
        }
    }

    private fun sendPushNotification(
        title: String,
        body: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
            notificationRepository.sendNotification(
                param = NotificationRequest(
                    fcmData = FcmRequest(
                        title = title,
                        body = body
                    ),
                    token = fcmToken
                )
            )
        }
    }

    private fun addMessageListener(addresseeId: String, senderId: String) {
        viewModelScope.launch(showErrorExceptionHandler) {
            messageRepository.addListener(
                addresseeId = addresseeId,
                senderId = senderId,
                ::onMessageEvent,
                ::onError
            )
        }
    }

    fun removeChatListener() {
        viewModelScope.launch(showErrorExceptionHandler) { messageRepository.removeListener() }
    }

    private fun onMessageEvent(wasMessageDeleted: Boolean, msg: Message) {
        if (wasMessageDeleted) messages.remove(msg.id) else messages[msg.id] = msg
    }

    private fun onLoggedUserEvent(user: User) {
        loggedUser.value = user
    }

}