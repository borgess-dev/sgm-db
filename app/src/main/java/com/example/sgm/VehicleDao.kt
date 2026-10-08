package com.example.sgm

import androidx.room3.Dao
import androidx.room3.Delete
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Transaction
import androidx.room3.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface VehicleDao {

    @Query("SELECT * FROM vehicles ORDER BY name ASC")
    fun getAllVehicles(): Flow<List<Vehicle>>

    @Transaction
    @Query("SELECT * FROM vehicles")
    fun getVehiclesWithGroup(): Flow<List<VehicleWithGroup>>

    @Query("SELECT * FROM vehicles WHERE id = :id LIMIT 1")
    suspend fun findById(id: Int): Vehicle?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vehicle: Vehicle): Long

    @Update
    suspend fun update(vehicle: Vehicle)

    @Delete
    suspend fun delete(vehicle: Vehicle)
}