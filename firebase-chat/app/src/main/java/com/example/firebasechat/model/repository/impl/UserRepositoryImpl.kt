package com.example.firebasechat.model.repository.impl

import com.example.firebasechat.model.User
import com.example.firebasechat.model.repository.UserRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(): UserRepository {
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

    override fun saveUser(user: User, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(USER_COLLECTION)
            .add(user)
            .addOnSuccessListener { result ->
                val modifiedUser = user.copy(id = result.id)
                updateUser(modifiedUser, onResult)
            }
    }

    override fun updateUser(user: User, onResult: (Throwable?) -> Unit) {
        Firebase.firestore
            .collection(USER_COLLECTION)
            .document(user.id)
            .set(user)
            .addOnCompleteListener { onResult(it.exception) }
    }

    override fun deleteUser(userId: String, onResult: (Throwable?) -> Unit) {
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

    override fun getUser(userId: String, onError: (Throwable) -> Unit, onSuccess: (User) -> Unit) {
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

    companion object {
        private const val USER_COLLECTION = "Users"
        private const val USER_ID = "userId"
        private const val USER_EMAIL = "userId"
    }
}