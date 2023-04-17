package com.octocat.network

import com.octocat.network.dto.RepoDto
import com.octocat.network.dto.UserDto
import com.octocat.network.exception.NoNetworkException
import com.octocat.network.manager.NetworkManager
import com.octocat.network.service.RepoService
import com.octocat.network.service.UserService
import javax.inject.Inject

class GithubClientImpl @Inject constructor(
    private val networkManager: NetworkManager,
    private val userService: UserService,
    private val repoService: RepoService,
) : GithubClient {

    override suspend fun getUser(
        username: String
    ): UserDto {
        if (!networkManager.isNetworkAvailable) {
            throw NoNetworkException
        }
        return userService.getUser(username)
    }

    override suspend fun getRepo(
        username: String,
        repoName: String
    ): RepoDto {
        if (!networkManager.isNetworkAvailable) {
            throw NoNetworkException
        }
        return repoService.getRepo(username, repoName)
    }

    override suspend fun getRepos(
        username: String
    ): List<RepoDto> {
        if (!networkManager.isNetworkAvailable) {
            throw NoNetworkException
        }
        return repoService.getRepos(username)
    }

}