package com.example.mostridatasca.ui.map

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mostridatasca.MostriDaTascaApplication
import com.example.mostridatasca.data.ObjectsRepository
import com.example.mostridatasca.data.ProfileRepository
import com.example.mostridatasca.data.UsersRepository
import com.example.mostridatasca.model.User
import com.example.mostridatasca.model.VirtualObject
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class MapUiState(
    val objects: List<VirtualObject> = emptyList(),
    val users: List<User> = emptyList(),
    val errorMessage: String = ""
)

class MapViewModel(
    private val dataStore: DataStore<Preferences>,
    private val profileRepository: ProfileRepository,
    private val objectsRepository: ObjectsRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                if (preferences[SID] == null) {
                    val session = try {
                        MonstersApi.retrofitService.getSession()
                    } catch (e: Exception) {
                        _uiState.value =
                            _uiState.value.copy(errorMessage = "Error getting session. Check your internet connection and restart the app")
                        return@edit
                    }
                    preferences[SID] = session.sid
                    preferences[UID] = session.uid
                    profileRepository.insertUser(
                        MonstersApi.retrofitService.getUser(
                            session.uid,
                            session.sid
                        )
                    )
                }
            }
            dataStore.data
                .catch {
                    _uiState.value =
                        _uiState.value.copy(errorMessage = "Error getting session. Check your internet connection and restart the app")
                }.collect {
                    Log.d("MapViewModel", "init, sid: ${it[SID]}, uid: ${it[UID]}")
                }
        }
        viewModelScope.launch {
            objectsRepository.nearbyObjects
                .catch {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error getting nearby objects. Check your internet connection and restart the app"
                    )
                }
                .collect {
                    _uiState.value = _uiState.value.copy(objects = it)
                }
        }
        viewModelScope.launch {
            usersRepository.nearbyUsers
                .catch {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error getting nearby users. Check your internet connection and restart the app"
                    )
                }
                .collect {
                    _uiState.value = _uiState.value.copy(users = it)
                }
        }
    }

    fun deleteError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }

    companion object {
        val SID = stringPreferencesKey("sid")
        val UID = intPreferencesKey("uid")
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MostriDaTascaApplication)
                MapViewModel(
                    application.container.dataStore,
                    application.container.profileRepository,
                    application.container.objectsRepository,
                    application.container.usersRepository
                )
            }
        }
    }
}