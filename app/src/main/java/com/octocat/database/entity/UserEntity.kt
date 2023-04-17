package com.octocat.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index("userId", unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey val username: String,
    val userId: Long,
    val name: String,
    val avatarUrl: String
)