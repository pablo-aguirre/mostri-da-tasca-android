package com.example.mostridatasca.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "objects")
data class VirtualObject(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: String,
    val level: Int,
    val lat: Double,
    val lon: Double,
    val image: String? = null
)
