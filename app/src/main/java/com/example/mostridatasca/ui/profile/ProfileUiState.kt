package com.example.mostridatasca.ui.profile

data class ProfileUiState(
    val name: String = "",
    val picture: String? = null,
    val positionShare: Boolean = false,
    val life: String = "100",
    val experience: String = "0",
    val newName: String = "",
    val isNewNameValid: Boolean = false
)