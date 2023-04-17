package com.octocat.domain

import com.octocat.domain.models.RepoInfo

interface RepoRepository {

    suspend fun getRepos(username: String): List<RepoInfo>

}