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
import com.example.mostridatasca.data.ProfileRepository
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.launch

class MapViewModel(
    private val dataStore: DataStore<Preferences>,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            try {
                dataStore.edit { preferences ->
                    if (preferences[SID] == null) {
                        val session = MonstersApi.retrofitService.getSession()
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
                dataStore.data.collect {
                    Log.d("MapViewModel", "init, sid: ${it[SID]}, uid: ${it[UID]}")
                }
            } catch (e: Exception) {
                Log.e("MapViewModel", "init: ${e.message}")
            }
        }
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
                    application.container.profileRepository
                )
            }
        }
    }
}