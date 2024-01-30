package com.example.mostridatasca.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val uid: Int = 0,
    val name: String = "",
    val lat: Double? = null,
    val lon: Double? = null,
    val life: Int = 0,
    val experience: Int = 0,
    val weapon: Int? = null,
    val armor: Int? = null,
    val amulet: Int? = null,
    val picture: String? = null,
    val profileversion: Int = 0,
    val positionshare: Boolean = false,
)