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
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UsersRepository(
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao
) {
    private val currentLeaderBoard = MutableStateFlow<List<Int>>(emptyList())
    val leaderBoard: Flow<List<User>> = userDao.getAllUsers().map {
        it.filter { user -> user.uid in currentLeaderBoard.value }
            .sortedBy { user -> user.experience }.reversed()
    }

    private companion object {
        val SID = stringPreferencesKey("sid")
        val UID = intPreferencesKey("uid")
    }

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
                Log.d("UsersRepository", "init, users: ${leaderBoard.last().size}")
            } catch (e: Exception) {
                Log.e("UsersRepository", "init: ${e.message}")
            }
        }
    }

    private suspend fun updateLeaderBoard() {
        dataStore.data.collectLatest { preferences ->
            val rankingList = MonstersApi.retrofitService.getRankingList(preferences[SID] ?: "")
            currentLeaderBoard.value = rankingList.map {
                if (userDao.getUser(it.uid) == null) {
                    val user = MonstersApi.retrofitService.getUser(it.uid, preferences[SID]!!)
                    userDao.insert(user)
                } else if (userDao.getUser(it.uid)!!.profileversion < it.profileversion) {
                    val user = MonstersApi.retrofitService.getUser(it.uid, preferences[SID]!!)
                    userDao.update(user)
                }
                it.uid
            }
        }
    }
}