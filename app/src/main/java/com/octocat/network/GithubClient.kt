package com.octocat.network

import com.octocat.network.dto.RepoDto
import com.octocat.network.dto.UserDto

interface GithubClient {
    suspend fun getUser(
        username: String
    ): UserDto

    suspend fun getRepo(
        username: String,
        repoName: String
    ): RepoDto

    suspend fun getRepos(
        username: String
    ): List<RepoDto>
}