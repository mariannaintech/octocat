package com.octocat.domain

import com.octocat.domain.models.UserInfo

interface UserRepository {

    suspend fun getUser(username: String): UserInfo

}