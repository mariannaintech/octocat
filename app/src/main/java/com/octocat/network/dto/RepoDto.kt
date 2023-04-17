package com.octocat.network.dto

import com.google.gson.annotations.SerializedName
import java.util.*

data class RepoDto(
    @SerializedName("id") val repoId: Long?,
    @SerializedName("owner") val owner: UserDto?,
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("updated_at") val updatedAt: Date?,
    @SerializedName("stargazers_count") val starsCount: Int?,
    @SerializedName("forks") val forksCount: Int?
)