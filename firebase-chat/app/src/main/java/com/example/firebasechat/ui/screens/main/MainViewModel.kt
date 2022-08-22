/*
Copyright 2022 Google LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    https://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.firebasechat.ui.screens.main

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.viewModelScope
import com.example.firebasechat.CHAT_SCREEN
import com.example.firebasechat.MAIN_SCREEN
import com.example.firebasechat.SETTINGS_SCREEN
import com.example.firebasechat.SIGN_UP_SCREEN
import com.example.firebasechat.model.User
import com.example.firebasechat.model.repository.AccountRepository
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.model.repository.UserStorageRepository
import com.example.firebasechat.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    logRepository: LogRepository,
    private val accountRepository: AccountRepository,
    private val userStorageRepository: UserStorageRepository
) : BaseViewModel(logRepository) {
    var users = mutableStateMapOf<String, User>()
        private set

    fun addUsersListener() {
        viewModelScope.launch(showErrorExceptionHandler) {
            userStorageRepository.addListener(accountRepository.getUserId(), ::onUserEvent, ::onError)
        }
    }

    fun removeUsersListener() {
        viewModelScope.launch(showErrorExceptionHandler) { userStorageRepository.removeListener() }
    }

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onUserActionClick(openScreen: (String) -> Unit, user: User) {
        // TODO: Pass user Id as a Parameter
        openScreen(CHAT_SCREEN)
    }

    private fun onUserEvent(wasDocumentDeleted: Boolean, user: User) {
        if (wasDocumentDeleted) users.remove(user.id) else users[user.id] = user
    }
}