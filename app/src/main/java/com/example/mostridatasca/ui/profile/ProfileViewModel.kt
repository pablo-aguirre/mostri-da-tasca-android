package com.example.mostridatasca.ui.profile

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mostridatasca.data.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import java.io.InputStream

class ProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    private val repository = ProfileRepository()

    init {
        getProfile()
    }

    private fun getProfile() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                val user = repository.getUser()
                _uiState.update {
                    it.copy(
                        name = user.name, picture = user.picture, positionShare = user.positionshare
                    )
                }
            } catch (e: HttpException) {
                _uiState.update { it.copy(error = true) }
            } catch (e: IOException) {
                _uiState.update { it.copy(error = true) }
            }
        }
        _uiState.update { it.copy(loading = false) }
    }

    fun setNewName(newName: String) {
        _uiState.update {
            it.copy(
                newName = newName,
                isNewNameValid = newName.isNotBlank() && newName.length <= 15
            )
        }
    }

    fun updateName(newName: String) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                repository.updateUser(
                    name = newName,
                    picture = uiState.value.picture,
                    positionShare = uiState.value.positionShare
                )
                _uiState.update { it.copy(name = newName, newName = "", isNewNameValid = false) }
            } catch (e: HttpException) {
                _uiState.update { it.copy(error = true) }
            } catch (e: IOException) {
                _uiState.update { it.copy(error = true) }
            }
        }
        _uiState.update { it.copy(loading = false) }
    }

    fun updatePositionShare(newValue: Boolean) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                repository.updateUser(
                    name = uiState.value.name,
                    picture = uiState.value.picture,
                    positionShare = newValue
                )
                _uiState.update { it.copy(positionShare = newValue) }
            } catch (e: HttpException) {
                _uiState.update { it.copy(error = true) }
            } catch (e: IOException) {
                _uiState.update { it.copy(error = true) }
            }
        }
        _uiState.update { it.copy(loading = false) }
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

        val imageString =
            imageBytes?.let { android.util.Base64.encodeToString(it, android.util.Base64.DEFAULT) }

        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                repository.updateUser(
                    name = uiState.value.name,
                    picture = imageString,
                    positionShare = uiState.value.positionShare
                )
                _uiState.update { it.copy(picture = imageString) }
            } catch (e: HttpException) {
                _uiState.update { it.copy(error = true) }
            } catch (e: IOException) {
                _uiState.update { it.copy(error = true) }
            }
        }
        inputStream?.close();
        _uiState.update { it.copy(loading = false) }
    }

}