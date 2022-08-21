package com.example.firebasechat.model.repository

import com.example.firebasechat.model.User

interface UserStorageRepository {
    fun addListener(
        userId: String,
        onDocumentEvent: (Boolean, User) -> Unit,
        onError: (Throwable) -> Unit
    )
    fun removeListener()
    fun saveUser(user: User, onResult: (Throwable?) -> Unit)
    fun updateUser(user: User, onResult: (Throwable?) -> Unit)
    fun deleteUser(userId: String, onResult: (Throwable?) -> Unit)
}