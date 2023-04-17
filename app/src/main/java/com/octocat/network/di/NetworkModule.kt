package com.octocat.network.di

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.octocat.network.GithubClient
import com.octocat.network.GithubClientImpl
import com.octocat.network.interceptor.ErrorInterceptor
import com.octocat.network.manager.NetworkManager
import com.octocat.network.manager.NetworkManagerImpl
import com.octocat.network.service.RepoService
import com.octocat.network.service.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    companion object {
        private const val BASE_URL = "https://api.github.com/"
        private const val DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    }

    @Provides
    @Singleton
    fun providesGson(): Gson = GsonBuilder()
        .setDateFormat(DATE_TIME).create()

    @Provides
    @Singleton
    fun providesOkHttpClient(gson: Gson): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ErrorInterceptor(gson))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providesUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun providesRepoService(retrofit: Retrofit): RepoService {
        return retrofit.create(RepoService::class.java)
    }

    @Provides
    @Singleton
    fun providesNetworkManager(application: Application): NetworkManager {
        return NetworkManagerImpl(application)
    }

    @Provides
    @Singleton
    fun providesGithubClient(
        networkManager: NetworkManager,
        userService: UserService,
        repoService: RepoService,
    ): GithubClient {
        return GithubClientImpl(networkManager, userService, repoService)
    }

}