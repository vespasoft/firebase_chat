package com.example.firebasechat.model.resources.firestore.impl

import com.example.firebasechat.model.resources.firestore.LogFirestoreDatabase
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class LogFirestoreDatabaseImpl @Inject constructor(): LogFirestoreDatabase {
    override fun recordException(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)
    }
}