package com.octocat.data

import com.octocat.data.mapper.dtoToEntity
import com.octocat.data.mapper.entityToDomain
import com.octocat.database.RepoDao
import com.octocat.domain.RepoRepository
import com.octocat.domain.models.RepoInfo
import com.octocat.network.GithubClient
import io.getstream.log.taggedLogger
import javax.inject.Inject

class RepoRepositoryImpl @Inject constructor(
    private val githubClient: GithubClient,
    private val repoDao: RepoDao,
) : RepoRepository {

    private val logger by taggedLogger("RepoRepository")

    override suspend fun getRepos(username: String): List<RepoInfo> {
        logger.d { "[getRepos] username: $username" }
        val existingRepos = repoDao.getRepos(username)
        if (existingRepos.isNotEmpty()) {
            logger.v { "[getRepos] found existing: ${existingRepos.size}" }
            return existingRepos.entityToDomain()
        }
        return githubClient.getRepos(username = username)
            .also { repoDao.insertRepos(it.dtoToEntity()) }
            .let { repoDao.getRepos(username) }
            .entityToDomain()
            .also { logger.v { "[getRepos] completed: ${it.size}" } }
    }
}