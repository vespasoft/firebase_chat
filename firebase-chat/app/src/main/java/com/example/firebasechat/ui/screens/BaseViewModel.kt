package com.example.firebasechat.ui.screens

import androidx.lifecycle.ViewModel
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.ui.common.snackbar.SnackbarManager
import com.example.firebasechat.ui.common.snackbar.SnackbarMessage.Companion.toSnackbarMessage
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel(private val logRepository: LogRepository) : ViewModel() {
    open val showErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError(throwable)
    }

    open val logErrorExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        logRepository.logNonFatalCrash(throwable)
    }

    open fun onError(error: Throwable) {
        SnackbarManager.showMessage(error.toSnackbarMessage())
        logRepository.logNonFatalCrash(error)
    }
}