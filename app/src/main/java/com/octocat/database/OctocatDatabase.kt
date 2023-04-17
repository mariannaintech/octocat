package com.octocat.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.octocat.database.converters.DateConverter
import com.octocat.database.entity.RepoEntity
import com.octocat.database.entity.UserEntity

@Database(
  entities = [UserEntity::class, RepoEntity::class],
  version = 1,
)
@TypeConverters(value = [DateConverter::class])
abstract class OctocatDatabase : RoomDatabase() {

  abstract fun userDao(): UserDao

  abstract fun repoDao(): RepoDao
}
