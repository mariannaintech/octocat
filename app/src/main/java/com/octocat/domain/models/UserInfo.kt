package com.octocat.domain.models

data class UserInfo(
    val userId: Long,
    val username: String,
    val name: String,
    val avatarUrl: String
)