package com.octocat.ui.home

import com.octocat.base.BaseTest
import com.octocat.coroutines.CoroutinesRule
import com.octocat.domain.home.HomeUseCase
import com.octocat.domain.models.HomeData
import io.getstream.log.StreamLog
import io.getstream.log.kotlin.KotlinStreamLogger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BaseTest() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var useCase: HomeUseCase

    @get:Rule
    val coroutinesRule = CoroutinesRule()

    @Before
    fun setup() {
        useCase = mock()
        viewModel = HomeViewModel(useCase)
    }

    @Test
    fun testFailedLoadData() = runTest {
        /* Given */
        val expectedFailure = TestException("test")
        val expectedResult = Result.failure<HomeData>(expectedFailure)
        val expectedState = HomeViewState.Failure(expectedFailure)
        whenever(useCase.loadData(any())) doReturn expectedResult

        /* When */
        viewModel.loadData("username")
        val actualState = viewModel.state.first { it != null }

        /* Then */
        verify(useCase).loadData(any())
        actualState shouldBeEqualTo expectedState
    }

}

data class TestException(override val message: String): RuntimeException()