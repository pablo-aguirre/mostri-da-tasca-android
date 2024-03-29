package com.example.mostridatasca.ui.objects

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mostridatasca.MostriDaTascaApplication
import com.example.mostridatasca.data.ObjectsRepository
import com.example.mostridatasca.data.ProfileRepository
import com.example.mostridatasca.location.LocationClient
import com.example.mostridatasca.model.VirtualObject
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class NearbyObjectUiState(
    val objects: List<VirtualObject> = emptyList(),
    val selectedObject: VirtualObject? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val errorMessage: String = ""
)

class NearbyObjectsViewModel(
    private val objectsRepository: ObjectsRepository,
    private val profileRepository: ProfileRepository,
    private val locationClient: LocationClient,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _uiState = MutableStateFlow(NearbyObjectUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            objectsRepository.nearbyObjects
                .catch {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error getting nearby objects. Check your internet connection and retry."
                    )
                }
                .collect {
                    _uiState.value = _uiState.value.copy(objects = it)
                }
        }
        viewModelScope.launch {
            locationClient.getLocationUpdates(5000)
                .catch {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Error getting location. Check your internet connection and retry."
                    )
                }
                .collect {
                    _uiState.value =
                        _uiState.value.copy(latitude = it.latitude, longitude = it.longitude)
                }
        }
    }

    fun selectObject(virtualObject: VirtualObject?) {
        _uiState.value = _uiState.value.copy(selectedObject = virtualObject)
    }

    fun isNear(virtualObject: VirtualObject): Boolean {
        val distance = calculateDistance(
            _uiState.value.latitude, _uiState.value.longitude, virtualObject.lat, virtualObject.lon
        )
        return distance < 100
    }

    private fun calculateDistance(
        lat1: Double, lon1: Double, lat2: Double, lon2: Double
    ): Double {
        val R = 6371.0 // Raggio medio della Terra in chilometri

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * sin(
                dLon / 2
            ) * sin(dLon / 2)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return R * c * 1000
    }

    fun activeObject() {
        viewModelScope.launch {
            try {
                dataStore.data.collect {
                    val result = MonstersApi.retrofitService.activateObject(
                        _uiState.value.selectedObject!!.id, it[SID]!!
                    )
                    Log.d("NearbyObjectsViewModel", "activeObject, result: $result")
                    if (result.died) {
                        profileRepository.resetUser()
                    } else {
                        profileRepository.updateUserStatus(
                            _uiState.value.selectedObject!!, result.life, result.experience
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Error activating object. Check your internet connection and retry."
                )
            }
        }
    }

    fun deleteError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }

    companion object {
        val SID = stringPreferencesKey("sid")
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MostriDaTascaApplication)
                NearbyObjectsViewModel(
                    application.container.objectsRepository,
                    application.container.profileRepository,
                    application.container.locationClient,
                    application.container.dataStore
                )
            }
        }
    }
}