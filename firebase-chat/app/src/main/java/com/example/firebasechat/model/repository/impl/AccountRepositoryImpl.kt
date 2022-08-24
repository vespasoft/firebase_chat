package com.example.firebasechat.model.repository.impl

import com.example.firebasechat.model.LoggedUser
import com.example.firebasechat.model.repository.AccountRepository
import com.example.firebasechat.model.resources.firestore.AccountFirestoreDatabase
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountFirestoreDatabase: AccountFirestoreDatabase
) : AccountRepository {
    override fun hasUser(): Boolean {
        return accountFirestoreDatabase.hasUser()
    }

    override fun isAnonymousUser(): Boolean {
        return accountFirestoreDatabase.isAnonymousUser()
    }

    override fun getUserId(): String {
        return accountFirestoreDatabase.getUserId()
    }

    override fun getLoggedUser(): LoggedUser {
        return accountFirestoreDatabase.getLoggedUser()
    }

    override fun authenticate(email: String, password: String, onResult: (Throwable?) -> Unit) {
        accountFirestoreDatabase.authenticate(email, password, onResult)
    }

    override fun createAccount(email: String, password: String, onResult: (Throwable?) -> Unit) {
        accountFirestoreDatabase.createAccount(email, password, onResult)
    }

    override fun sendRecoveryEmail(email: String, onResult: (Throwable?) -> Unit) {
        accountFirestoreDatabase.sendRecoveryEmail(email, onResult)
    }

    override fun createAnonymousAccount(onResult: (Throwable?) -> Unit) {
        accountFirestoreDatabase.createAnonymousAccount(onResult)
    }

    override fun linkAccount(email: String, password: String, onResult: (Throwable?) -> Unit) {
        accountFirestoreDatabase.linkAccount(email, password, onResult)
    }

    override fun deleteAccount(onResult: (Throwable?) -> Unit) {
        accountFirestoreDatabase.deleteAccount(onResult)
    }

    override fun signOut() {
        accountFirestoreDatabase.signOut()
    }
}