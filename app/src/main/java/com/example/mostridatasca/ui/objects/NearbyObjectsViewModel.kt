package com.example.mostridatasca.ui.objects

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mostridatasca.MostriDaTascaApplication
import com.example.mostridatasca.location.LocationClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NearbyObjectUiState(
    val latitude: String = "",
    val longitude: String = "",
)

class NearbyObjectsViewModel(
    private val locationClient: LocationClient
) : ViewModel() {
    private val _uiState = MutableStateFlow(NearbyObjectUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                locationClient.getLocationUpdates(1000)
                    .collect {
                        _uiState.value = _uiState.value.copy(
                            latitude = it.latitude.toString(),
                            longitude = it.longitude.toString()
                        )
                        Log.d(
                            "LocationClient",
                            "latitude: ${it.latitude}, longitude: ${it.longitude}"
                        )
                    }
            } catch (e: LocationClient.LocationException) {
                Log.e("LocationClient", "Error while getting location updates", e)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MostriDaTascaApplication)
                NearbyObjectsViewModel(application.container.locationClient)
            }
        }
    }
}