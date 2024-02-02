package com.example.mostridatasca.model

data class ActiveObjectResponse(
    val died: Boolean,
    val life: Int,
    val experience: Int,
    val weapon: Int?,
    val armor: Int?,
    val amulet: Int?
)