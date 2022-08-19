package com.example.firebasechat.ui.screens.splash

import com.example.firebasechat.CHAT_SCREEN
import com.example.firebasechat.SPLASH_SCREEN
import com.example.firebasechat.model.repository.AccountRepository
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val logRepository: LogRepository
) : BaseViewModel(logRepository) {
    fun onAppStart(openAndPopUp: (String, String) -> Unit) {
        if (accountRepository.hasUser()) openAndPopUp(CHAT_SCREEN, SPLASH_SCREEN)
        else {
            //TODO: Navigate to LogIn or SignUp screen
            openAndPopUp(CHAT_SCREEN, SPLASH_SCREEN)
        }
    }
}