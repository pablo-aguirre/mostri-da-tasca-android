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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


data class LeaderBoardUiState(
    val users: List<User> = emptyList(),
    val selectedUser: User? = null,
    val errorMessage: String = ""
)

class LeaderBoardViewModel(
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LeaderBoardUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            usersRepository.leaderBoard
                .catch {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error getting leader board. Check your internet connection and restart the app"
                    )
                }
                .collect {
                    _uiState.value = _uiState.value.copy(users = it)
                }
        }
        updateLeaderBoard()
    }

    fun selectUser(user: User?) {
        Log.d("LeaderBoardViewModel", "selectUser: $user")
        _uiState.value = _uiState.value.copy(selectedUser = user)
    }

    fun updateLeaderBoard() {
        viewModelScope.launch {
            try {
                usersRepository.updateLeaderBoard()
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(errorMessage = "Error updating leader board. Check your internet connection and restart the app")
            }
        }
    }

    fun deleteError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MostriDaTascaApplication)
                LeaderBoardViewModel(application.container.usersRepository)
            }
        }
    }
}