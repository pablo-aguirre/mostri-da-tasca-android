package com.example.mostridatasca.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mostridatasca.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("UPDATE users SET name = :name WHERE uid = :uid")
    suspend fun updateName(uid: Int, name: String)

    @Query("UPDATE users SET picture = :picture WHERE uid = :uid")
    suspend fun updatePicture(uid: Int, picture: String?)

    @Query("UPDATE users SET positionshare = :positionshare WHERE uid = :uid")
    suspend fun updatePositionShare(uid: Int, positionshare: Boolean)

    @Query("UPDATE users SET weapon = :weapon WHERE uid = :uid")
    suspend fun updateWeapon(uid: Int, weapon: Int)

    @Query("UPDATE users SET armor = :armor WHERE uid = :uid")
    suspend fun updateArmor(uid: Int, armor: Int)

    @Query("UPDATE users SET amulet = :amulet WHERE uid = :uid")
    suspend fun updateAmulet(uid: Int, amulet: Int)

    @Query("UPDATE users SET life = :life, experience = :experience WHERE uid = :uid")
    suspend fun updateStatus(uid: Int, life: Int, experience: Int)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * from users WHERE uid = :uid")
    fun getUser(uid: Int): User?

    @Query("SELECT * from users")
    fun getAllUsers(): Flow<List<User>>

    @Query("DELETE FROM users")
    fun deleteAll()

    @Query("UPDATE users SET profileversion = profileversion + 1 WHERE uid = :uid")
    suspend fun updateVersion(uid: Int)
}