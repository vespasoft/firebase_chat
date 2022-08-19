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

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.firebasechat.SETTINGS_SCREEN
import com.example.firebasechat.SPLASH_SCREEN
import com.example.firebasechat.model.LoggedUser
import com.example.firebasechat.model.repository.AccountRepository
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    logRepository: LogRepository,
    private val accountRepository: AccountRepository
) : BaseViewModel(logRepository) {

    var currentUser = mutableStateOf(LoggedUser())

    init {
        addLoggedUserListener()
    }

    private fun addLoggedUserListener() {
        viewModelScope.launch {
            currentUser.value = accountRepository.getLoggedUser()
        }
    }

    fun onSettingsClick(openScreen: (String) -> Unit) = openScreen(SETTINGS_SCREEN)

    fun onSignOutClick(restartApp: (String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            accountRepository.signOut()
            restartApp(SPLASH_SCREEN)
        }
    }
}