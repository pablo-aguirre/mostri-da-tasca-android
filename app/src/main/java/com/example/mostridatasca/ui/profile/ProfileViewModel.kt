package com.example.mostridatasca.ui.profile

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mostridatasca.MostriDaTascaApplication
import com.example.mostridatasca.data.ProfileRepository
import com.example.mostridatasca.model.VirtualObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

data class ProfileUiState(
    val name: String = "",
    val picture: String? = null,
    val positionShare: Boolean = false,
    val life: String = "100",
    val experience: String = "0",
    val artifacts: List<VirtualObject> = emptyList(),
    val newName: String = "",
    val isNewNameValid: Boolean = false,
    val errorMessage: String = ""
)

class ProfileViewModel(
    private val profileRepository: ProfileRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.profile.collect {
                _uiState.value = _uiState.value.copy(
                    name = it.name,
                    picture = it.picture,
                    positionShare = it.positionshare,
                    experience = it.experience.toString(),
                    life = it.life.toString()
                )
            }
        }
        viewModelScope.launch {
            profileRepository.artifacts.collect {
                _uiState.value = _uiState.value.copy(artifacts = it)
            }
        }
    }

    fun setNewName(newName: String) {
        _uiState.update {
            it.copy(
                newName = newName,
                isNewNameValid = newName.isNotBlank() && newName.length <= 15 && newName != it.name
            )
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            try {
                profileRepository.updateUser(
                    newName,
                    _uiState.value.picture,
                    _uiState.value.positionShare
                )
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(errorMessage = "Error updating name. Check your internet connection and retry.")
            }
            _uiState.value = _uiState.value.copy(newName = "")
        }
    }

    fun updatePositionShare(newValue: Boolean) {
        viewModelScope.launch {
            try {
                profileRepository.updateUser(
                    _uiState.value.name,
                    _uiState.value.picture,
                    newValue
                )
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(errorMessage = "Error updating position share. Check your internet connection and retry.")
            }
        }
    }

    fun updatePicture(contentResolver: ContentResolver, pictureUri: Uri?) {
        File(pictureUri?.path ?: return).length().let {
            if (it > 100000) return
        }
        val inputStream: InputStream? = contentResolver.openInputStream(pictureUri)
        val imageBytes = inputStream?.readBytes()
        inputStream?.close()

        val imageString =
            imageBytes?.let { android.util.Base64.encodeToString(it, android.util.Base64.DEFAULT) }

        viewModelScope.launch {
            try {
                profileRepository.updateUser(
                    _uiState.value.name,
                    imageString,
                    _uiState.value.positionShare
                )
            } catch (e: Exception) {
                _uiState.value =
                    _uiState.value.copy(errorMessage = "Error updating image.")
            }
        }
    }

    fun deleteError() {
        _uiState.value = _uiState.value.copy(errorMessage = "")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MostriDaTascaApplication)
                ProfileViewModel(application.container.profileRepository)
            }
        }
    }
}