package com.octocat.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(
    indices = [
        Index(value = ["username", "name"], unique = true)
    ]
)
data class RepoEntity(
    val username: String,
    @PrimaryKey val repoId: Long,
    val name: String,
    val description: String,
    val updatedAt: Date?,
    val starsCount: Int,
    val forksCount: Int
)