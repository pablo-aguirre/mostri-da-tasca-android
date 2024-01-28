package com.example.mostridatasca.ui.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mostridatasca.data.UsersRepository
import com.example.mostridatasca.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class LeaderBoardUiState(
    val users: List<User> = emptyList()
)

class LeaderBoardViewModel(
    private val usersRepository: UsersRepository = UsersRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(LeaderBoardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            usersRepository.observeUsers().collect {
                _uiState.value = _uiState.value.copy(users = it)
                Log.d("LeaderBoardViewModel", "init, users: ${_uiState.value.users.size}")
            }
        }
    }
}