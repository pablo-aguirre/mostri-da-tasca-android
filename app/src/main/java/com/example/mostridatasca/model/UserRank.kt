package com.example.mostridatasca.model

data class UserRank(
    val uid: Int = 0,
    val life: Int = 0,
    val experience: Int = 0,
    val profileversion: Int = 0,
    val positionshare: Boolean = false,
    val lat: Double? = null,
    val lon: Double? = null,
)
