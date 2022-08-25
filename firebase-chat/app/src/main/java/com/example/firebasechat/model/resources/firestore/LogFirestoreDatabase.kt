package com.example.firebasechat.model.resources.firestore

interface LogFirestoreDatabase {
    fun recordException(throwable: Throwable)
}