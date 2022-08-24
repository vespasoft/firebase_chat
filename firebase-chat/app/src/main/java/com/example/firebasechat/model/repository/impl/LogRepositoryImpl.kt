package com.example.firebasechat.model.repository.impl

import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.model.resources.firestore.LogFirestoreDatabase
import javax.inject.Inject

class LogRepositoryImpl @Inject constructor(
    private val logFirestoreDatabase: LogFirestoreDatabase
) : LogRepository {
    override fun logNonFatalCrash(throwable: Throwable) {
        logFirestoreDatabase.recordException(throwable)
    }
}