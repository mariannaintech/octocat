package com.octocat.database

import android.os.Build.VERSION_CODES.M
import com.octocat.coroutines.CoroutinesRule
import com.octocat.database.entity.UserEntity
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
class UserDaoTest : AbstractDatabaseTest() {

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    private lateinit var userDao: UserDao

    @Before
    fun setup() {
        userDao = db.userDao()
    }

    @Test
    fun insertAndLoadPokemonListTest() = runTest {
        val expectedUser = UserEntity(
            username = "octocat",
            userId = 123,
            name = "Octocat",
            avatarUrl = ""
        )
        userDao.insertUser(expectedUser)

        val actualUser = userDao.getUser(expectedUser.username)

        actualUser shouldBeEqualTo expectedUser
    }
}
