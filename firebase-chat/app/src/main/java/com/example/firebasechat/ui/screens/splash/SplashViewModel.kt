package com.example.firebasechat.ui.screens.splash

import com.example.firebasechat.LOGIN_SCREEN
import com.example.firebasechat.MAIN_SCREEN
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
        if (accountRepository.hasUser()) openAndPopUp(MAIN_SCREEN, SPLASH_SCREEN)
        else {
            openAndPopUp(LOGIN_SCREEN, SPLASH_SCREEN)
        }
    }
}