package com.example.mostridatasca.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mostridatasca.data.local.UserDao
import com.example.mostridatasca.location.LocationClient
import com.example.mostridatasca.model.User
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UsersRepository(
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao,
    private val locationClient: LocationClient
) {
    private val currentLeaderBoard = MutableStateFlow<List<Int>>(emptyList())
    val leaderBoard: Flow<List<User>> = userDao.getAllUsers().map {
        it.filter { user -> user.uid in currentLeaderBoard.value }
            .sortedBy { user -> user.experience }.reversed()
    }

    val nearbyUsers: Flow<List<User>> =
        locationClient.getLocationUpdates(5000).combine(dataStore.data) { location, preferences ->
            val nearbyUsers = MonstersApi.retrofitService.nearbyUsers(
                preferences[SID]!!,
                location.latitude,
                location.longitude
            )
            nearbyUsers.forEach {
                if (userDao.getUser(it.uid) == null) {
                    val user = MonstersApi.retrofitService.getUser(it.uid, preferences[SID]!!)
                    userDao.insert(user)
                } else if (userDao.getUser(it.uid)!!.profileversion < it.profileversion) {
                    val user = MonstersApi.retrofitService.getUser(it.uid, preferences[SID]!!)
                    userDao.update(user)
                }
            }
            nearbyUsers.map { it.uid }
        }.combine(userDao.getAllUsers()) { currentNearbyUsers, users ->
            users.filter { it.uid in currentNearbyUsers }
        }

    private companion object {
        val SID = stringPreferencesKey("sid")
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                updateLeaderBoard()
                Log.d("UsersRepository", "init, users: ${leaderBoard.last().size}")
            } catch (e: Exception) {
                Log.e("UsersRepository", "init: ${e.message}")
            }
        }
    }

    private suspend fun updateLeaderBoard() {
        dataStore.data.collect { preferences ->
            val rankingList = MonstersApi.retrofitService.getRankingList(preferences[SID]!!)
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