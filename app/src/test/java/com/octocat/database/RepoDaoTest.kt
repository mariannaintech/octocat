package com.octocat.database

import android.os.Build.VERSION_CODES.M
import com.octocat.coroutines.CoroutinesRule
import com.octocat.database.entity.RepoEntity
import java.util.Date
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [M])
class RepoDaoTest : AbstractDatabaseTest() {

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    private lateinit var repoDao: RepoDao

    @Before
    fun setup() {
        repoDao = db.repoDao()
    }

    @Test
    fun insertAndLoadPokemonListTest() = runTest {
        val expectedRepo = RepoEntity(
            username = "octocat",
            repoId = 123,
            name = "SuperRepo",
            description = "description",
            updatedAt = Date(),
            starsCount = 100,
            forksCount = 1,
        )
        repoDao.insertRepos(listOf(expectedRepo))

        val actualRepos = repoDao.getRepos(expectedRepo.username)

        actualRepos shouldBeEqualTo listOf(expectedRepo)
    }
}
