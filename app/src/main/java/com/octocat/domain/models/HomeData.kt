package com.octocat.domain.models

data class HomeData(
    val user: UserInfo,
    val repos: List<RepoInfo>,
) {

    override fun toString(): String {
        return "HomeData(user=${user.username}, repos.size=${repos.size})"
    }
}
