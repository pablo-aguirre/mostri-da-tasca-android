package com.example.mostridatasca

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mostridatasca.data.ProfileRepository

private const val SESSION = "session"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = SESSION
)

class MostriDaTascaApplication : Application() {
    lateinit var profileRepository: ProfileRepository

    override fun onCreate() {
        super.onCreate()
        profileRepository = ProfileRepository(dataStore = dataStore)
    }
}