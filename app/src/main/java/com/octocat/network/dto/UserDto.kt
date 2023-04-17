package com.octocat.network.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") val userId: Long?,
    @SerializedName("login") val username: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
)