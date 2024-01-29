package com.example.mostridatasca.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mostridatasca.model.User
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val SID = stringPreferencesKey("sid")
        val UID = intPreferencesKey("uid")
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataStore.edit {
                    if (it[SID] == null || it[UID] == null) {
                        val session = MonstersApi.retrofitService.getSession()
                        it[SID] = session.sid
                        it[UID] = session.uid
                    }
                    Log.d("ProfileRepository", "init, session: ${it[SID]}, ${it[UID]}")
                    profile.value = MonstersApi.retrofitService.getUser(it[UID] ?: 0, it[SID] ?: "")
                }
                Log.d("ProfileRepository", "init, profile: ${profile.value}")
            } catch (e: Exception) {
                Log.e("ProfileRepository", "init: ${e.message}")
            }
        }
    }

    private val profile = MutableStateFlow(User())

    fun observerProfile(): Flow<User> = profile

    suspend fun updateUser(
        name: String = profile.value.name,
        picture: String? = profile.value.picture,
        positionShare: Boolean = profile.value.positionshare
    ) {
        dataStore.data.collectLatest {
            try {
                MonstersApi.retrofitService.updateUser(
                    id = it[UID] ?: 0,
                    sid = it[SID] ?: "",
                    name = name,
                    picture = picture,
                    positionshare = positionShare
                )
                profile.value = profile.value.copy(
                    name = name,
                    picture = picture,
                    positionshare = positionShare
                )
            } catch (e: Exception) {
                Log.e("ProfileRepository", "updateUser: ${e.message}")
            }
        }
    }
}