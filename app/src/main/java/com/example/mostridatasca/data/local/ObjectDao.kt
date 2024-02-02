package com.example.mostridatasca.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mostridatasca.model.VirtualObject
import kotlinx.coroutines.flow.Flow

@Dao
interface ObjectDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(virtualObject: VirtualObject)

    @Query("SELECT * from objects WHERE id = :id")
    fun getObject(id: Int): VirtualObject?

    @Query("SELECT * from objects")
    fun getAllObjects(): Flow<List<VirtualObject>>
}
