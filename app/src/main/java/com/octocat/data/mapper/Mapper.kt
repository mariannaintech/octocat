package com.octocat.data.mapper

import com.octocat.database.entity.RepoEntity
import com.octocat.database.entity.UserEntity
import com.octocat.network.dto.RepoDto
import com.octocat.network.dto.UserDto
import com.octocat.domain.models.RepoInfo
import com.octocat.domain.models.UserInfo

fun UserDto.toDomain() = UserInfo(
    userId = userId ?: error("userId is missing in UserDto: $this"),
    username = username ?: error("username is missing in UserDto: $this"),
    name = name.orEmpty(),
    avatarUrl = avatarUrl.orEmpty()
)

fun RepoDto.toDomain() = RepoInfo(
    repoId = repoId ?: error("repoId is missing in RepoDto: $this"),
    name = name.orEmpty(),
    description = description.orEmpty(),
    updatedAt = updatedAt,
    starsCount = starsCount ?: 0,
    forksCount = forksCount ?: 0
)

fun UserDto.toEntity() = UserEntity(
    userId = userId ?: error("userId is missing in UserDto: $this"),
    username = username ?: error("username is missing in UserDto: $this"),
    name = name.orEmpty(),
    avatarUrl = avatarUrl.orEmpty()
)

fun RepoDto.toEntity() = RepoEntity(
    repoId = repoId ?: error("repoId is missing in RepoDto: $this"),
    username = owner?.username ?: error("username is missing in RepoDto: $this"),
    name = name.orEmpty(),
    description = description.orEmpty(),
    updatedAt = updatedAt,
    starsCount = starsCount ?: 0,
    forksCount = forksCount ?: 0
)

fun UserEntity.toDomain() = UserInfo(
    userId = userId ,
    username = username,
    name = name,
    avatarUrl = avatarUrl
)

fun RepoEntity.toDomain() = RepoInfo(
    repoId = repoId,
    name = name,
    description = description,
    updatedAt = updatedAt,
    starsCount = starsCount,
    forksCount = forksCount
)

fun List<RepoDto>.dtoToEntity() = map { it.toEntity() }

fun List<RepoDto>.dtoToDomain() = map { it.toDomain() }

fun List<RepoEntity>.entityToDomain() = map { it.toDomain() }