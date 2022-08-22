package com.example.firebasechat.ui.screens.chat

import androidx.compose.runtime.mutableStateOf
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    logRepository: LogRepository
): BaseViewModel(logRepository) {
    private val initialMessages = listOf<Message>()

    var uiState = mutableStateOf(
        ChatUiState(
            chatTitle = "Contact's name",
            initialMessages = initialMessages
        )
    )
        private set

}