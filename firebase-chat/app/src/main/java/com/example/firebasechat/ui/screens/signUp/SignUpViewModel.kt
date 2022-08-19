package com.example.firebasechat.ui.screens.signUp

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.firebasechat.CHAT_SCREEN
import com.example.firebasechat.LOGIN_SCREEN
import com.example.firebasechat.SIGN_UP_SCREEN
import com.example.firebasechat.model.repository.AccountRepository
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.ui.common.ext.isValidEmail
import com.example.firebasechat.ui.common.ext.isValidPassword
import com.example.firebasechat.ui.common.ext.passwordMatches
import com.example.firebasechat.ui.common.snackbar.SnackbarManager
import com.example.firebasechat.ui.screens.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.firebasechat.R.string as AppText

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val logRepository: LogRepository
) : BaseViewModel(logRepository) {
    var uiState = mutableStateOf(SignUpUiState())
        private set

    private val email get() = uiState.value.email
    private val password get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onRepeatPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(repeatPassword = newValue)
    }

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage(AppText.email_error)
            return
        }

        if (!password.isValidPassword()) {
            SnackbarManager.showMessage(AppText.password_error)
            return
        }

        if (!password.passwordMatches(uiState.value.repeatPassword)) {
            SnackbarManager.showMessage(AppText.password_match_error)
            return
        }

        viewModelScope.launch(showErrorExceptionHandler) {
            accountRepository.createAccount(email, password) { error ->
                if (error == null) {
                    linkWithEmail()
                    openAndPopUp(CHAT_SCREEN, SIGN_UP_SCREEN)
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

    fun onLoginClick(openAndPopUp: (String, String) -> Unit) {
        viewModelScope.launch(showErrorExceptionHandler) {
            openAndPopUp(LOGIN_SCREEN, SIGN_UP_SCREEN)
        }
    }
}