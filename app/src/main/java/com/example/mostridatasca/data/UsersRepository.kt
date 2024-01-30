package com.example.mostridatasca.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mostridatasca.data.local.UserDao
import com.example.mostridatasca.model.User
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UsersRepository(
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao
) {
    private val leaderBoard = MutableStateFlow<List<User>>(emptyList())

    private companion object {
        val SID = stringPreferencesKey("sid")
        val UID = intPreferencesKey("uid")
    }

    fun observeUsers(): Flow<List<User>> = leaderBoard

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataStore.edit { preferences ->
                    if (preferences[SID] == null || preferences[UID] == null) {
                        val session = MonstersApi.retrofitService.getSession()
                        preferences[SID] = session.sid
                        preferences[UID] = session.uid
                    }
                    updateLeaderBoard()
                }
                Log.d("UsersRepository", "init, users: ${leaderBoard.value.size}")
            } catch (e: Exception) {
                Log.e("UsersRepository", "init: ${e.message}")
            }
        }
    }

    private suspend fun updateLeaderBoard() {
        dataStore.data.collectLatest { preferences ->
            val rankingList = MonstersApi.retrofitService.getRankingList(preferences[SID] ?: "")
            leaderBoard.value = rankingList.map {
                if (userDao.getUser(it.uid) == null || userDao.getUser(it.uid)!!.profileversion < it.profileversion) {
                    try {
                        val user =
                            MonstersApi.retrofitService.getUser(it.uid, preferences[SID] ?: "")
                        userDao.insert(user)
                        Log.d("UsersRepository", "updateLeaderBoard, insert: ${user.uid}")
                    } catch (e: Exception) {
                        Log.e("UsersRepository", "updateLeaderBoard: ${e.message}")
                    }
                }
                userDao.getUser(it.uid)!!
            }
        }
    }
}