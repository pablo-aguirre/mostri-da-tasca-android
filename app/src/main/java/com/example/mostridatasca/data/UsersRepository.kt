package com.example.mostridatasca.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mostridatasca.data.local.UserDao
import com.example.mostridatasca.location.LocationClient
import com.example.mostridatasca.model.User
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class UsersRepository(
    private val dataStore: DataStore<Preferences>, private val userDao: UserDao, private val locationClient: LocationClient
) {
    private val currentLeaderBoard = MutableStateFlow<List<Int>>(emptyList())
    val leaderBoard: Flow<List<User>> = userDao.getAllUsers().combine(currentLeaderBoard) { users, ids ->
        users.filter { user -> user.uid in ids }.sortedBy { user -> user.experience }.reversed()
    }

    val nearbyUsers: Flow<List<User>> =
        locationClient.getLocationUpdates(5000).combine(dataStore.data) { location, preferences ->
            val nearbyUsers = MonstersApi.retrofitService.nearbyUsers(
                preferences[SID]!!, location.latitude, location.longitude
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

    suspend fun updateLeaderBoard() {
        dataStore.data.collect { preferences ->
            val rankingList = MonstersApi.retrofitService.getRankingList(preferences[SID]!!)
            currentLeaderBoard.value = rankingList.map {
                if (userDao.getUser(it.uid) == null) {
                    val newUser = MonstersApi.retrofitService.getUser(it.uid, preferences[SID]!!)
                    userDao.insert(newUser)
                } else if (userDao.getUser(it.uid)!!.profileversion < it.profileversion) {
                    val user = MonstersApi.retrofitService.getUser(it.uid, preferences[SID]!!)
                    userDao.update(user)
                }
                it.uid
            }
        }
    }

    private companion object {
        val SID = stringPreferencesKey("sid")
    }
}