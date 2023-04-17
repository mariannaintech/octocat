package com.octocat.data.di

import com.octocat.data.RepoRepositoryImpl
import com.octocat.data.UserRepositoryImpl
import com.octocat.domain.RepoRepository
import com.octocat.domain.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindsUserRepository(repository: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun bindsRepoRepository(repository: RepoRepositoryImpl): RepoRepository

}