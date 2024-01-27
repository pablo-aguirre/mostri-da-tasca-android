package com.example.mostridatasca.data

import android.util.Log
import com.example.mostridatasca.model.Profile
import com.example.mostridatasca.model.Session
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileRepository {
    private val session = MutableStateFlow(Session())
    private val profile = MutableStateFlow(Profile())

    suspend fun getUser(): Profile { // TODO: save in local db, if not present
        session.value = MonstersApi.retrofitService.getSession()
        profile.value = MonstersApi.retrofitService.getUser(
            id = session.value.uid,
            sid = session.value.sid
        )
        Log.d("ProfileRepository", "getUser: ${session.value}")
        Log.d("ProfileRepository", "getUser: ${profile.value}")
        return profile.value
    }

    suspend fun updateUser(
        name: String,
        picture: String?,
        positionShare: Boolean
    ) {
        Log.d("ProfileRepository", "updateUser: $name, $picture, $positionShare")
        MonstersApi.retrofitService.updateUser(
            id = session.value.uid,
            sid = session.value.sid,
            name = name,
            picture = picture,
            positionshare = positionShare
        )
    }
}