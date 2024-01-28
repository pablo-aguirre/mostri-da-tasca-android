package com.example.mostridatasca.model

data class User(
    val uid: Int = 0,
    val name: String = "",
    val life: Int = 0,
    val experience: Int = 0,
    val weapon: Int? = null,
    val armor: Int? = null,
    val amulet: Int? = null,
    val picture: String? = null,
    val profileversion: Int = 0,
    val positionshare: Boolean = false,
)