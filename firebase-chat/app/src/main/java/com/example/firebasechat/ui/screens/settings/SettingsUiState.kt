package com.example.firebasechat.ui.screens.settings

import com.example.firebasechat.model.LoggedUser

data class SettingsUiState(
    val loggedUser: LoggedUser? = LoggedUser()
)
