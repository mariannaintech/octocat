package com.octocat.network.service

import com.octocat.network.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {

    @GET("users/{id}")
    suspend fun getUser(@Path("id") username: String): UserDto

}