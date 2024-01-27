package com.example.mostridatasca.ui.profile

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mostridatasca.data.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.InputStream

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    private val repository = ProfileRepository()

    init {
        viewModelScope.launch {
            val user = repository.getUser()
            _uiState.update {
                it.copy(
                    name = user.name, picture = user.picture, positionShare = user.positionshare
                )
            }
        }
    }

    fun setNewName(newName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                newName = newName, isNewNameValid = newName.isNotBlank() && newName.length <= 15
            )
        }
    }

    fun updateName(newName: String) {
        viewModelScope.launch {
            try {
                repository.updateUser(
                    name = newName,
                    picture = uiState.value.picture,
                    positionShare = uiState.value.positionShare
                )
                _uiState.update { it.copy(name = newName, newName = "", isNewNameValid = false) }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "updateName: ", e)
            }
        }
    }

    fun updatePositionShare(newValue: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateUser(
                    name = uiState.value.name,
                    picture = uiState.value.picture,
                    positionShare = newValue
                )
                _uiState.update { it.copy(positionShare = newValue) }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "updatePositionShare: ", e)
            }
        }
    }

    fun updatePicture(contentResolver: ContentResolver, pictureUri: Uri?) {
        val inputStream: InputStream? = contentResolver.openInputStream(pictureUri ?: return)
        val imageBytes = inputStream?.readBytes()

        val imageString =
            imageBytes?.let { android.util.Base64.encodeToString(it, android.util.Base64.DEFAULT) }
        viewModelScope.launch {
            try {
                repository.updateUser(
                    name = uiState.value.name,
                    picture = imageString,
                    positionShare = uiState.value.positionShare
                )
                _uiState.update { it.copy(picture = imageString) }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "updatePicture: ", e)
            }
        }
        inputStream?.close();
    }

}