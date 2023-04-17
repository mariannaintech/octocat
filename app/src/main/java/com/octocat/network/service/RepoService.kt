package com.octocat.network.service

import com.octocat.network.dto.RepoDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RepoService {

    @GET("repos/{username}/{repoName}")
    suspend fun getRepo(
        @Path("username") username: String,
        @Path("repoName") repoName: String
    ): RepoDto

    @GET("users/{id}/repos")
    suspend fun getRepos(@Path("id") username: String): List<RepoDto>

}