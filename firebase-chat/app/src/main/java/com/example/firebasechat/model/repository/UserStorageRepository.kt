package com.example.firebasechat.model.repository

import com.example.firebasechat.model.User

interface UserStorageRepository {
    fun getUsers(onError: (Throwable) -> Unit, onSuccess: (User) -> Unit)
    fun saveUser(user: User, onResult: (Throwable?) -> Unit)
    fun updateUser(user: User, onResult: (Throwable?) -> Unit)
    fun deleteUser(userId: String, onResult: (Throwable?) -> Unit)
}