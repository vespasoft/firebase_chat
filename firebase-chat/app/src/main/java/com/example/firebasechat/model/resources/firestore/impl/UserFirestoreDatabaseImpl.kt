package com.example.firebasechat.model.resources.firestore.impl

import com.example.firebasechat.model.User
import com.example.firebasechat.model.repository.NotificationRepository
import com.example.firebasechat.model.resources.firestore.NotificationCloudMessage
import com.example.firebasechat.model.resources.firestore.UserFirestoreDatabase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class UserFirestoreDatabaseImpl @Inject constructor(
    private val notificationCloudMessage: NotificationCloudMessage
): UserFirestoreDatabase {
    private var listenerRegistration: ListenerRegistration? = null
    private var loggedUserListenerRegistration: ListenerRegistration? = null

    override fun addListener(
        userId: String,
        onUserEvent: (Boolean, User) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val query = Firebase.firestore.collection(USER_COLLECTION).whereNotEqualTo(USER_ID, userId)

        listenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            value?.documentChanges?.forEach {
                val wasUserDeleted = it.type == DocumentChange.Type.REMOVED
                val document = it.document.toObject<User>().copy(id = it.document.id)
                onUserEvent(wasUserDeleted, document)
            }
        }
    }

    override fun addLoggedUserListener(
        userId: String,
        onLoggedUserEvent: (User) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val query = Firebase.firestore.collection(USER_COLLECTION).whereEqualTo(USER_ID, userId)

        loggedUserListenerRegistration = query.addSnapshotListener { value, error ->
            if (error != null) {
                onError(error)
                return@addSnapshotListener
            }

            value?.documentChanges?.forEach {
                val document = it.document.toObject<User>().copy(id = it.document.id)
                onLoggedUserEvent(document)
            }
        }
    }

    override fun removeListener() {
        listenerRegistration?.remove()
        loggedUserListenerRegistration?.remove()
    }

    override fun saveUser(
        user: User,
        onResult: (Throwable?) -> Unit
    ) {
        notificationCloudMessage.fetchingFCMRegistrationToken { fcmToken ->
            val updatedUser = user.copy(fcmToken = fcmToken)
            Firebase.firestore
                .collection(USER_COLLECTION)
                .add(updatedUser)
                .addOnSuccessListener { result ->
                    val modifiedUser = updatedUser.copy(id = result.id)
                    updateUser(modifiedUser, onResult)
                }
        }
    }

    override fun updateUser(
        user: User,
        onResult: (Throwable?) -> Unit
    ) {
        Firebase.firestore
            .collection(USER_COLLECTION)
            .document(user.id)
            .set(user)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteUser(
        userId: String,
        onResult: (Throwable?) -> Unit
    ) {
        Firebase.firestore
            .collection(USER_COLLECTION)
            .whereEqualTo(USER_ID, userId)
            .get()
            .addOnFailureListener { error -> onResult(error) }
            .addOnSuccessListener { result ->
                for (document in result) document.reference.delete()
                onResult(null)
            }
    }

    override fun getUser(
        userId: String,
        onError: (Throwable) -> Unit,
        onSuccess: (User) -> Unit
    ) {
        Firebase.firestore
            .collection(USER_COLLECTION)
            .document(userId)
            .get()
            .addOnFailureListener { error -> onError(error) }
            .addOnSuccessListener { result ->
                val user = result.toObject<User>()?.copy(id = result.id)
                onSuccess(user ?: User())
            }
    }

    override fun registerFCMToken(
        user: User,
        onError: (Throwable) -> Unit,
        onSuccess: () -> Unit
    ) {
        notificationCloudMessage.fetchingFCMRegistrationToken { fcmToken ->
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