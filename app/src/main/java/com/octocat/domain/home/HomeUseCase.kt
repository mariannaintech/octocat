package com.octocat.domain.home

import com.octocat.domain.models.HomeData

interface HomeUseCase {

    suspend fun loadData(username: String): Result<HomeData>

}