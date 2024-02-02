package com.example.mostridatasca.ui.objects

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mostridatasca.MostriDaTascaApplication
import com.example.mostridatasca.data.ObjectsRepository
import com.example.mostridatasca.model.VirtualObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class NearbyObjectUiState(
    val objects: List<VirtualObject> = emptyList()
)

class NearbyObjectsViewModel(
    private val objectsRepository: ObjectsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NearbyObjectUiState())
    val uiState = _uiState.asStateFlow()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            objectsRepository.nearbyObjects.collect {
                _uiState.value = _uiState.value.copy(objects = it)
                Log.d("NearbyObjectsViewModel", "init, objects: ${_uiState.value.objects.size}")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MostriDaTascaApplication)
                NearbyObjectsViewModel(application.container.objectsRepository)
            }
        }
    }
}