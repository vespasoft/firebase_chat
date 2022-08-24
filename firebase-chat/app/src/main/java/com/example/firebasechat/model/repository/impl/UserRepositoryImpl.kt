package com.example.firebasechat.model.repository.impl

import com.example.firebasechat.model.User
import com.example.firebasechat.model.repository.NotificationRepository
import com.example.firebasechat.model.repository.UserRepository
import com.example.firebasechat.model.resources.firestore.UserFirestoreDatabase
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userFirestoreDatabase: UserFirestoreDatabase,
    private val notificationRepository: NotificationRepository
): UserRepository {

    override fun addListener(
        userId: String,
        onUserEvent: (Boolean, User) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        userFirestoreDatabase.addListener(userId, onUserEvent, onError)
    }

    override fun addLoggedUserListener(
        userId: String,
        onLoggedUserEvent: (User) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        userFirestoreDatabase.addLoggedUserListener(userId, onLoggedUserEvent, onError)
    }

    override fun removeListener() {
        userFirestoreDatabase.removeListener()
    }

    override fun saveUser(
        user: User,
        onResult: (Throwable?) -> Unit
    ) {
        userFirestoreDatabase.saveUser(user, onResult)
    }

    override fun updateUser(
        user: User,
        onResult: (Throwable?) -> Unit
    ) {
        userFirestoreDatabase.updateUser(user, onResult)
    }

    override fun deleteUser(
        userId: String,
        onResult: (Throwable?) -> Unit
    ) {
        userFirestoreDatabase.deleteUser(userId, onResult)
    }

    override fun getUser(
        userId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (User) -> Unit
    ) {
        userFirestoreDatabase.getUser(userId, onError, onSuccess)
    }

    override fun registerFCMToken(
        user: User,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        notificationRepository.fetchingFCMRegistrationToken { fcmToken ->
            val modifiedUser = user.copy(
                fcmToken = fcmToken
            )

            if (modifiedUser == null) {
                onError(Throwable("userId invalid"))
            }

            if (modifiedUser != null) {
                updateUser(modifiedUser) {
                    if (it != null){
                        onError(it)
                    }

                    if (it == null) {
                        onSuccess()
                    }
                }
            }
        }
    }

    companion object {
        private const val USER_COLLECTION = "Users"
        private const val USER_ID = "userId"
    }
}