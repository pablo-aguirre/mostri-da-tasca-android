package com.example.mostridatasca.ui.profile

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.InputStream

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun setNewName(newName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                newName = newName,
                isNewNameValid = newName.isNotBlank() && newName.length <= 15
            )
        }
    }

    fun updateName(newName: String) {
        _uiState.update { currentState ->
            currentState.copy(
                name = newName,
                newName = "",
                isNewNameValid = false
            )
        }
    }

    fun updatePositionShare(newValue: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(positionShare = newValue)
        }
    }

    fun updatePicture(contentResolver: ContentResolver, pictureUri: Uri?) {
        val inputStream: InputStream? = contentResolver.openInputStream(pictureUri ?: return)
        val imageBytes = inputStream?.readBytes()

        val imageString =
            imageBytes?.let { android.util.Base64.encodeToString(it, android.util.Base64.DEFAULT) }
        _uiState.update { currentState ->
            currentState.copy(picture = imageString)
        }
        inputStream?.close();
    }

}