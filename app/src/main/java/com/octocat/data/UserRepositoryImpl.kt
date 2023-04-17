package com.octocat.data

import com.octocat.data.mapper.toDomain
import com.octocat.data.mapper.toEntity
import com.octocat.database.UserDao
import com.octocat.domain.UserRepository
import com.octocat.domain.models.UserInfo
import com.octocat.network.GithubClient
import io.getstream.log.taggedLogger
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val githubClient: GithubClient,
    private val userDao: UserDao,
) : UserRepository {

    private val logger by taggedLogger("UserRepository")

    override suspend fun getUser(username: String): UserInfo {
        logger.d { "[getUser] username: $username" }
        val existingUser = userDao.getUser(username)
        if (existingUser != null) {
            logger.v { "[getUser] found existing: $existingUser" }
            return existingUser.toDomain()
        }
        return  githubClient.getUser(username = username)
            .also { userDao.insertUser(it.toEntity()) }
            .toDomain()
            .also { logger.v { "[getUser] completed: $it" } }
    }

}