package com.example.mostridatasca.data

import android.util.Log
import com.example.mostridatasca.model.Profile
import com.example.mostridatasca.model.Session
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProfileRepository {
    private var session: Session = Session()
    private val profile = MutableStateFlow(Profile())

    fun observerProfile(): Flow<Profile> = profile

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                session = MonstersApi.retrofitService.getSession()
                profile.value = MonstersApi.retrofitService.getUser(session.uid, session.sid)
                Log.d("ProfileRepository", "init, session: $session")
                Log.d("ProfileRepository", "init, profile: ${profile.value}")
            } catch (e: Exception) {
                Log.e("ProfileRepository", "init: ${e.message}")
            }
        }
    }

    suspend fun updateUser(
        name: String = profile.value.name,
        picture: String? = profile.value.picture,
        positionShare: Boolean = profile.value.positionshare
    ) {
        Log.d("ProfileRepository", "updateUser: $name, $picture, $positionShare")
        try {
            MonstersApi.retrofitService.updateUser(
                id = session.uid,
                sid = session.sid,
                name = name,
                picture = picture,
                positionshare = positionShare
            )
            profile.value = profile.value.copy(
                name = name,
                picture = picture,
                positionshare = positionShare
            )
        } catch (e: Exception) {
            Log.e("ProfileRepository", "updateUser: ${e.message}")
        }
    }
}