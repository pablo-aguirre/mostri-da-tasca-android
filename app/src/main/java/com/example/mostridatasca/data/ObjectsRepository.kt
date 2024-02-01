package com.example.mostridatasca.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mostridatasca.data.local.ObjectDao
import com.example.mostridatasca.location.LocationClient
import com.example.mostridatasca.model.VirtualObject
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ObjectsRepository(
    private val dataStore: DataStore<Preferences>,
    private val objectDao: ObjectDao,
    private val locationClient: LocationClient,
) {
    private val currentNearbyObjects = MutableStateFlow<List<Int>>(emptyList())
    val nearbyObjects: Flow<List<VirtualObject>> = objectDao.getAllObjects().map {
        it.filter { virtualObject -> virtualObject.id in currentNearbyObjects.value }
    }

    private companion object {
        val SID = stringPreferencesKey("sid")
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                locationClient.getLocationUpdates(5000)
                    .combine(dataStore.data) { location, preferences ->
                        Log.d(
                            "ObjectsRepository",
                            "init, location: ${location.latitude}, ${location.longitude}"
                        )
                        val nearbyObjects = MonstersApi.retrofitService.getNearbyObjects(
                            location.latitude, location.longitude, preferences[SID]!!
                        )
                        nearbyObjects.forEach {
                            if (objectDao.getObject(it.id) == null) {
                                Log.d("ObjectsRepository", "init, insert: ${it.id}")
                                val virtualObject =
                                    MonstersApi.retrofitService.getObject(it.id, preferences[SID]!!)
                                objectDao.insert(virtualObject)
                            }
                        }
                        nearbyObjects.map { it.id }
                    }.collect {
                        currentNearbyObjects.value = it
                    }
            } catch (e: LocationClient.LocationException) {
                Log.e("LocationClient", "Error while getting location updates", e)
            }
        }
    }

    fun updateObjects() {
        CoroutineScope(Dispatchers.IO).launch {
            try {

            } catch (e: Exception) {
                Log.e("ObjectsRepository", "init: ${e.message}")
            }
        }
    }
}