package com.octocat.domain.di

import com.octocat.domain.home.HomeUseCase
import com.octocat.domain.home.HomeUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindsHomeUseCase(repository: HomeUseCaseImpl): HomeUseCase
}