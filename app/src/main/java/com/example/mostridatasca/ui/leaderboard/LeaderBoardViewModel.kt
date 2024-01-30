package com.example.mostridatasca.ui.leaderboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mostridatasca.MostriDaTascaApplication
import com.example.mostridatasca.data.UsersRepository
import com.example.mostridatasca.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class LeaderBoardUiState(
    val users: List<User> = emptyList(),
    val selectedUser: User? = null
)

class LeaderBoardViewModel(
    private val usersRepository: UsersRepository
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

    fun selectUser(user: User?) {
        Log.d("LeaderBoardViewModel", "selectUser: $user")
        _uiState.value = _uiState.value.copy(selectedUser = user)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MostriDaTascaApplication)
                LeaderBoardViewModel(application.usersRepository)
            }
        }
    }
}