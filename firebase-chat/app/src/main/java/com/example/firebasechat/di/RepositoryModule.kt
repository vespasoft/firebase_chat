package com.example.firebasechat.di

import com.example.firebasechat.model.repository.AccountRepository
import com.example.firebasechat.model.repository.LogRepository
import com.example.firebasechat.model.repository.UserStorageRepository
import com.example.firebasechat.model.repository.impl.AccountRepositoryImpl
import com.example.firebasechat.model.repository.impl.LogRepositoryImpl
import com.example.firebasechat.model.repository.impl.UserStorageRepositoryImpl
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
    abstract fun provideUserStorageRepository(impl: UserStorageRepositoryImpl): UserStorageRepository
}