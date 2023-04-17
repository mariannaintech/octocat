package com.octocat.database.di

import android.app.Application
import androidx.room.Room
import com.octocat.database.OctocatDatabase
import com.octocat.database.RepoDao
import com.octocat.database.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        application: Application,
    ): OctocatDatabase {
        return Room
            .databaseBuilder(application, OctocatDatabase::class.java, "octocat.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun providesUserDao(appDatabase: OctocatDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Singleton
    @Provides
    fun provideRepoDao(appDatabase: OctocatDatabase): RepoDao {
        return appDatabase.repoDao()
    }

}