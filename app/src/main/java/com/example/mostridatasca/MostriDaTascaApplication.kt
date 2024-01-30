package com.example.mostridatasca

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mostridatasca.data.ProfileRepository
import com.example.mostridatasca.data.UsersRepository

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "SID_UID"
)

class MostriDaTascaApplication : Application() {
    lateinit var profileRepository: ProfileRepository
    lateinit var usersRepository: UsersRepository

    override fun onCreate() {
        super.onCreate()
        profileRepository = ProfileRepository(dataStore = dataStore)
        usersRepository = UsersRepository(dataStore = dataStore)
    }
}