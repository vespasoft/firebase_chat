package com.example.firebasechat.model.repository.impl

import com.example.firebasechat.model.User
import com.example.firebasechat.model.repository.UserStorageRepository
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class UserStorageRepositoryImpl @Inject constructor(): UserStorageRepository {
    private var listenerRegistration: ListenerRegistration? = null

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
                val task = it.document.toObject<User>().copy(id = it.document.id)
                onUserEvent(wasUserDeleted, task)
            }
        }
    }

    override fun removeListener() {
        listenerRegistration?.remove()
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

    companion object {
        private const val USER_COLLECTION = "Users"
        private const val USER_ID = "userId"
    }
}