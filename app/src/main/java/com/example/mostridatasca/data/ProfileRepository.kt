package com.example.mostridatasca.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mostridatasca.data.local.ObjectDao
import com.example.mostridatasca.data.local.UserDao
import com.example.mostridatasca.model.User
import com.example.mostridatasca.model.VirtualObject
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class ProfileRepository(
    private val dataStore: DataStore<Preferences>,
    private val userDao: UserDao,
    private val objectDao: ObjectDao
) {
    private companion object {
        val SID = stringPreferencesKey("sid")
        val UID = intPreferencesKey("uid")
    }

    val profile: Flow<User> = userDao.getAllUsers().combine(dataStore.data) { users, preferences ->
        val uid = preferences[UID] ?: 0
        users.find { it.uid == uid } ?: User()
    }

    val artifacts: Flow<List<VirtualObject>> =
        userDao.getAllUsers().combine(dataStore.data) { users, preferences ->
            users.filter { it.uid == preferences[UID] }
        }.combine(objectDao.getAllObjects()) { users, virtualObjects ->
            val myUser = users.get(0)
            virtualObjects.filter { it.id == myUser.weapon || it.id == myUser.amulet || it.id == myUser.armor }
        }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                dataStore.edit {
                    if (it[SID] == null || it[UID] == null) {
                        val session = MonstersApi.retrofitService.getSession()
                        it[SID] = session.sid
                        it[UID] = session.uid
                        userDao.insert(
                            MonstersApi.retrofitService.getUser(
                                session.uid,
                                session.sid
                            )
                        )
                    }
                    Log.d("ProfileRepository", "init, session: ${it[SID]}, ${it[UID]}")
                }
                Log.d("ProfileRepository", "init, profile: ${profile.last()}")
            } catch (e: Exception) {
                Log.e("ProfileRepository", "init: ${e.message}")
            }
        }
    }

    suspend fun updateUser(
        name: String,
        picture: String?,
        positionShare: Boolean
    ) {
        dataStore.data.collectLatest {
            try {
                Log.d(
                    "ProfileRepository",
                    "updateUser: ${it[UID]}, $name, $picture, $positionShare"
                )
                MonstersApi.retrofitService.updateUser(
                    it[UID]!!,
                    it[SID]!!,
                    name,
                    picture,
                    positionShare
                )
                userDao.updateName(it[UID]!!, name)
                userDao.updatePicture(it[UID]!!, picture)
                userDao.updatePositionShare(it[UID]!!, positionShare)
            } catch (e: Exception) {
                Log.e("ProfileRepository", "updateUser: ${e.message}")
            }
        }
    }

    suspend fun updateUserStatus(virtualObject: VirtualObject, life: Int, experience: Int) {
        dataStore.data.collectLatest {
            when (virtualObject.type) {
                "weapon" -> userDao.updateWeapon(it[UID]!!, virtualObject.id)
                "armor" -> userDao.updateArmor(it[UID]!!, virtualObject.id)
                "amulet" -> userDao.updateAmulet(it[UID]!!, virtualObject.id)
            }
            userDao.updateLife(it[UID]!!, life)
            userDao.updateExperience(it[UID]!!, experience)
        }
    }
}