package com.example.firebasechat.di

import com.example.firebasechat.model.resources.remote.SendNotificationRemote
import com.example.firebasechat.model.resources.remote.impl.SendNotificationRemoteImpl
import com.example.firebasechat.model.repository.*
import com.example.firebasechat.model.repository.impl.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideAccountRepository(impl: AccountRepositoryImpl): AccountRepository

    @Binds
    abstract fun provideLogRepository(impl: LogRepositoryImpl): LogRepository

    @Binds
    abstract fun provideUserStorageRepository(impl: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun provideMessageRepository(impl: MessageRepositoryImpl): MessageRepository

    @Binds
    abstract fun provideSendNotificationClient(impl: SendNotificationRemoteImpl): SendNotificationRemote

    @Binds
    abstract fun provideNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository
}