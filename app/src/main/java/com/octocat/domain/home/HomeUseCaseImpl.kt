package com.octocat.domain.home

import com.octocat.domain.RepoRepository
import com.octocat.domain.UserRepository
import com.octocat.domain.di.ScopeQualifier
import com.octocat.domain.di.Scopes
import com.octocat.domain.models.HomeData
import io.getstream.log.taggedLogger
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async

class HomeUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val repoRepository: RepoRepository,
    @ScopeQualifier(Scopes.DOMAIN)
    private val domainScope: CoroutineScope,
) : HomeUseCase {

    private val logger by taggedLogger("HomeUseCase")

    override suspend fun loadData(username: String): Result<HomeData> {
        try {
            logger.d { "[loadData] username: $username" }
            val deferredUser = domainScope.async { userRepository.getUser(username) }
            val deferredRepo = domainScope.async { repoRepository.getRepos(username) }
            val data = HomeData(deferredUser.await(), deferredRepo.await())
            logger.v { "[loadData] completed: $data" }
            return Result.success(data)
        } catch (e: Throwable) {
            logger.e { "[loadData] failed: $e" }
            return Result.failure(e)
        }
    }

}