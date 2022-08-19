package com.example.firebasechat.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.firebasechat.CHAT_SCREEN
import com.example.firebasechat.LOGIN_SCREEN
import com.example.firebasechat.MAIN_SCREEN
import com.example.firebasechat.SIGN_UP_SCREEN
import com.example.firebasechat.model.repository.AccountRepository
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.ui.common.ext.isValidEmail
import com.example.firebasechat.ui.common.snackbar.SnackbarManager
import com.example.firebasechat.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.firebasechat.R.string as AppText

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val logRepository: LogRepository
) : BaseViewModel(logRepository) {
    var uiState = mutableStateOf(LoginUiState())
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage(AppText.empty_password_error)
            return
        }

        viewModelScope.launch(showErrorExceptionHandler) {
            accountRepository.authenticate(email, password) { error ->
                if (error == null) {
                    linkWithEmail()
                    openAndPopUp(MAIN_SCREEN, LOGIN_SCREEN)
                } else onError(error)
            }
        }
    }

    private fun linkWithEmail() {
        viewModelScope.launch(showErrorExceptionHandler) {
            accountRepository.linkAccount(email, password) { error ->
                if (error != null) logRepository.logNonFatalCrash(error)
            }
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        viewModelScope.launch(showErrorExceptionHandler) {
            accountRepository.sendRecoveryEmail(email) { error ->
                if (error != null) onError(error)
                else SnackbarManager.showMessage(AppText.recovery_email_sent)
            }
        }
    }

    fun onCreateAccountClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            openAndPopUp(SIGN_UP_SCREEN, LOGIN_SCREEN)
        }
    }
}