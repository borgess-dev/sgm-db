package com.example.sgm

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {

    @Query("SELECT * FROM groups ORDER BY name ASC")
    fun getAllGroups(): Flow<List<Group>>

    @Query("SELECT * FROM groups WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): Group?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(group: Group): Long

    @Update
    suspend fun update(group: Group)

    @Delete
    suspend fun delete(group: Group)
}