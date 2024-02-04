package com.example.mostridatasca.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mostridatasca.data.local.ObjectDao
import com.example.mostridatasca.data.local.UserDao
import com.example.mostridatasca.model.User
import com.example.mostridatasca.model.VirtualObject
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

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
        users.find { it.uid == preferences[UID] } ?: User()
    }

    val artifacts: Flow<List<VirtualObject>> =
        userDao.getAllUsers().combine(dataStore.data) { users, preferences ->
            users.find { it.uid == preferences[UID] }
        }.combine(objectDao.getAllObjects()) { myUser, virtualObjects ->
            virtualObjects.filter { it.id == myUser?.weapon || it.id == myUser?.amulet || it.id == myUser?.armor }
        }

    suspend fun updateUser(
        name: String,
        picture: String?,
        positionShare: Boolean
    ) {
        dataStore.data.collect {
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
            userDao.updateVersion(it[UID]!!)
        }
    }

    suspend fun updateUserStatus(virtualObject: VirtualObject, life: Int, experience: Int) {
        dataStore.data.collect {
            when (virtualObject.type) {
                "weapon" -> userDao.updateWeapon(it[UID]!!, virtualObject.id)
                "armor" -> userDao.updateArmor(it[UID]!!, virtualObject.id)
                "amulet" -> userDao.updateAmulet(it[UID]!!, virtualObject.id)
            }
            userDao.updateStatus(it[UID]!!, life, experience)
        }
    }

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }
}