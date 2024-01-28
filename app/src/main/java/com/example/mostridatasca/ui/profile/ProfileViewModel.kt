package com.example.mostridatasca.ui.profile

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mostridatasca.data.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream

class ProfileViewModel(
    private val profileRepository: ProfileRepository = ProfileRepository()
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            profileRepository.observerProfile().collect {
                _uiState.value = _uiState.value.copy(
                    name = it.name,
                    picture = it.picture,
                    positionShare = it.positionshare,
                    experience = it.experience.toString(),
                    life = it.life.toString()
                )
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
            profileRepository.updateUser(name = newName)
        }
        _uiState.update { it.copy(newName = "", isNewNameValid = false) }
    }

    fun updatePositionShare(newValue: Boolean) {
        viewModelScope.launch {
            profileRepository.updateUser(positionShare = newValue)
        }
    }

    fun updatePicture(contentResolver: ContentResolver, pictureUri: Uri?) {
        File(pictureUri?.path ?: return).length().let {
            if (it > 100000) {
                _uiState.update { it.copy(error = true) }
                return
            }
        }
        val inputStream: InputStream? = contentResolver.openInputStream(pictureUri ?: return)
        val imageBytes = inputStream?.readBytes()
        inputStream?.close()

        val imageString =
            imageBytes?.let { android.util.Base64.encodeToString(it, android.util.Base64.DEFAULT) }

        viewModelScope.launch {
            profileRepository.updateUser(picture = imageString)
        }
    }

}