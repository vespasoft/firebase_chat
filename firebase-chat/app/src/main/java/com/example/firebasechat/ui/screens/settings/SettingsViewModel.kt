package com.example.firebasechat.ui.screens.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.firebasechat.SPLASH_SCREEN
import com.example.firebasechat.model.repository.AccountRepository
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.model.repository.UserRepository
import com.example.firebasechat.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    logRepository: LogRepository,
    private val accountRepository: AccountRepository,
    private val userRepository: UserRepository
) : BaseViewModel(logRepository) {
    var uiState = mutableStateOf(SettingsUiState())
        private set

    fun initialize() {
        uiState.value = SettingsUiState(accountRepository.getLoggedUser())
    }

    fun onSignOutClick(restartApp: (String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            accountRepository.signOut()
            restartApp(SPLASH_SCREEN)
        }
    }

    fun onDeleteMyAccountClick(restartApp: (String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            userRepository.deleteUser(accountRepository.getUserId()) { error ->
                if (error == null) deleteAccount(restartApp) else onError(error)
            }
        }
    }

    private fun deleteAccount(restartApp: (String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            accountRepository.deleteAccount { error ->
                if (error == null) restartApp(SPLASH_SCREEN) else onError(error)
            }
        }
    }
}