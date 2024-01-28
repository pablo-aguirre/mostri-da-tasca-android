package com.example.mostridatasca.data

import android.util.Log
import com.example.mostridatasca.model.User
import com.example.mostridatasca.network.MonstersApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UsersRepository {
    private val users = MutableStateFlow<List<User>>(emptyList())

    fun observeUsers(): Flow<List<User>> = users

    init {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val sid = MonstersApi.retrofitService.getSession().sid
                val rankingList = MonstersApi.retrofitService.getRankingList(sid)
                val usersList = rankingList.map { MonstersApi.retrofitService.getUser(it.uid, sid) }
                users.value = usersList
                Log.d("UsersRepository", "init, users: ${users.value.size}")
            } catch (e: Exception) {
                Log.e("UsersRepository", "init: ${e.message}")
            }
        }
    }
}