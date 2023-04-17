package com.octocat.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.octocat.domain.home.HomeUseCase
import com.octocat.domain.models.HomeData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.log.taggedLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase
) : ViewModel() {

    private val logger by taggedLogger("HomeViewModel")

    private val _state = MutableStateFlow<HomeViewState?>(null)
    val state: StateFlow<HomeViewState?> = _state

    fun loadData(username: String) {
        viewModelScope.launch {
            logger.d { "[loadData] username: $username" }
            useCase.loadData(username)
                .onSuccess {
                    logger.v { "[loadData] completed: $it" }
                    _state.value = HomeViewState.Success(it)
                }
                .onFailure {
                    logger.e { "[loadData] failed: $it" }
                    _state.value = HomeViewState.Failure(it)
                }
        }
    }

}

sealed class HomeViewState {
    data class Success(val data: HomeData) : HomeViewState()
    data class Failure(val cause: Throwable) : HomeViewState()
}