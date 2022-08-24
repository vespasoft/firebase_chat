package com.example.firebasechat.di

import com.example.firebasechat.model.resources.firestore.*
import com.example.firebasechat.model.resources.firestore.impl.*
import com.example.firebasechat.model.resources.remote.SendNotificationRemote
import com.example.firebasechat.model.resources.remote.impl.SendNotificationRemoteImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResourceModule {

    @Binds
    abstract fun provideSendNotificationRemote(impl: SendNotificationRemoteImpl): SendNotificationRemote

    @Binds
    abstract fun provideNotificationCloudMessage(impl: NotificationCloudMessageImpl): NotificationCloudMessage

    @Binds
    abstract fun provideUserFirestoreDatabase(impl: UserFirestoreDatabaseImpl): UserFirestoreDatabase

    @Binds
    abstract fun provideAccountFirestoreDatabase(impl: AccountFirestoreDatabaseImpl): AccountFirestoreDatabase

    @Binds
    abstract fun provideLogFirestoreDatabase(impl: LogFirestoreDatabaseImpl): LogFirestoreDatabase

    @Binds
    abstract fun provideMessageFirestoreDatabase(impl: MessageFirestoreDatabaseImpl): MessageFirestoreDatabase
}