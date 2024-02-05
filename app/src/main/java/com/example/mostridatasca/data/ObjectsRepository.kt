package com.example.mostridatasca.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.mostridatasca.data.local.ObjectDao
import com.example.mostridatasca.location.LocationClient
import com.example.mostridatasca.model.VirtualObject
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ObjectsRepository(
    private val dataStore: DataStore<Preferences>,
    private val objectDao: ObjectDao,
    private val locationClient: LocationClient,
) {
    val nearbyObjects: Flow<List<VirtualObject>> =
        locationClient.getLocationUpdates(5000).combine(dataStore.data) { location, preferences ->
            val nearbyObjects = MonstersApi.retrofitService.getNearbyObjects(
                location.latitude, location.longitude, preferences[SID]!!
            )
            nearbyObjects.forEach {
                if (objectDao.getObject(it.id) == null) {
                    val virtualObject = MonstersApi.retrofitService.getObject(it.id, preferences[SID]!!)
                    objectDao.insert(virtualObject)
                    Log.d("ObjectsRepository", "insert object: ${it.id}")
                }
            }
            nearbyObjects.map { it.id }
        }.combine(objectDao.getAllObjects()) { nearbyObjects, objects ->
            objects.filter { it.id in nearbyObjects }
        }


    private companion object {
        val SID = stringPreferencesKey("sid")
    }

}