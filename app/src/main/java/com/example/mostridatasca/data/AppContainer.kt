package com.example.mostridatasca.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mostridatasca.data.local.AppDatabase

interface AppContainer {
    val usersRepository: UsersRepository
    val profileRepository: ProfileRepository
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "SID_UID"
)

class AppDataContainer(private val context: Context) : AppContainer {

    override val usersRepository: UsersRepository =
        UsersRepository(
            dataStore = context.dataStore,
            userDao = AppDatabase.getDatabase(context).userDao()
        )

    override val profileRepository: ProfileRepository =
        ProfileRepository(dataStore = context.dataStore)
}