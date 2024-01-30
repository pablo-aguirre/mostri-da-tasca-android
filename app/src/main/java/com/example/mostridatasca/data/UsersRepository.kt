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
import kotlinx.coroutines.launch

class UsersRepository(
    private val dataStore: DataStore<Preferences>
) {
    private val users = MutableStateFlow<List<User>>(emptyList())

    private companion object {
        val SID = stringPreferencesKey("sid")
        val UID = intPreferencesKey("uid")
    }

    fun observeUsers(): Flow<List<User>> = users

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataStore.edit { preferences ->
                    if (preferences[SID] == null || preferences[UID] == null) {
                        val session = MonstersApi.retrofitService.getSession()
                        preferences[SID] = session.sid
                        preferences[UID] = session.uid
                    }
                    val rankingList =
                        MonstersApi.retrofitService.getRankingList(preferences[SID] ?: "")
                    val usersList =
                        rankingList.map {
                            MonstersApi.retrofitService.getUser(it.uid, preferences[SID] ?: "")
                        }
                    users.value = usersList
                }
                Log.d("UsersRepository", "init, users: ${users.value.size}")
            } catch (e: Exception) {
                Log.e("UsersRepository", "init: ${e.message}")
            }
        }
    }
}