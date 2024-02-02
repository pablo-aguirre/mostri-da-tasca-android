package com.example.mostridatasca.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mostridatasca.data.local.AppDatabase
import com.example.mostridatasca.location.DefaultLocationClient
import com.example.mostridatasca.location.LocationClient
import com.google.android.gms.location.LocationServices

interface AppContainer {
    val usersRepository: UsersRepository
    val profileRepository: ProfileRepository
    val objectsRepository: ObjectsRepository
    val locationClient: LocationClient
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = "SID_UID"
)

class AppDataContainer(private val context: Context) : AppContainer {

    override val locationClient: LocationClient = DefaultLocationClient(
        context.applicationContext,
        LocationServices.getFusedLocationProviderClient(context.applicationContext)
    )

    override val usersRepository: UsersRepository = UsersRepository(
        dataStore = context.dataStore, userDao = AppDatabase.getDatabase(context).userDao()
    )

    override val profileRepository: ProfileRepository = ProfileRepository(
        dataStore = context.dataStore, userDao = AppDatabase.getDatabase(context).userDao()
    )

    override val objectsRepository: ObjectsRepository = ObjectsRepository(
        dataStore = context.dataStore,
        objectDao = AppDatabase.getDatabase(context).objectDao(),
        locationClient = locationClient
    )
}