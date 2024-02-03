package com.example.mostridatasca.model

data class UserNearby(
    val uid: Int,
    val lat: Double,
    val lon: Double,
    val profileversion: Int,
    val life: Int,
    val experience: Int,
    val time: String
)
