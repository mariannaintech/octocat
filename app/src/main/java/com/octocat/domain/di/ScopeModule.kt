package com.octocat.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.Dispatchers as CoroutineDispatchers

@Module
@InstallIn(SingletonComponent::class)
class ScopeModule {

    @Singleton
    @Provides
    fun providesDispatchers(): Dispatchers {
        return object : Dispatchers {
            override val mainDispatcher: CoroutineDispatcher
                get() = CoroutineDispatchers.Main
            override val defaultDispatcher: CoroutineDispatcher
                get() = CoroutineDispatchers.Default
            override val ioDispatcher: CoroutineDispatcher
                get() = CoroutineDispatchers.Default

        }
    }

    @Singleton
    @Provides
    @ScopeQualifier(Scopes.DOMAIN)
    fun providesDomainScope(dispatchers: Dispatchers): CoroutineScope {
        return CoroutineScope(SupervisorJob() + dispatchers.ioDispatcher)
    }

}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ScopeQualifier(val scopes: Scopes)

enum class Scopes {
    DOMAIN
}


interface Dispatchers {

    val mainDispatcher: CoroutineDispatcher

    val defaultDispatcher: CoroutineDispatcher

    val ioDispatcher: CoroutineDispatcher

}