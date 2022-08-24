package com.example.firebasechat.model.resources.firestore

import com.example.firebasechat.model.User

interface UserFirestoreDatabase {
    fun addListener(
        userId: String,
        onUserEvent: (Boolean, User) -> Unit,
        onError: (Throwable) -> Unit
    )
    fun addLoggedUserListener(
        userId: String,
        onLoggedUserEvent: (User) -> Unit,
        onError: (Throwable) -> Unit
    )
    fun removeListener()
    fun saveUser(user: User, onResult: (Throwable?) -> Unit)
    fun updateUser(user: User, onResult: (Throwable?) -> Unit)
    fun deleteUser(userId: String, onResult: (Throwable?) -> Unit)
    fun getUser(userId: String, onError: (Throwable) -> Unit, onSuccess: (User) -> Unit)
    fun registerFCMToken(user: User, onError: (Throwable) -> Unit, onSuccess: () -> Unit)
}