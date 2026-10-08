package com.example.sgm

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey

@Entity(
    tableName = "vehicles",
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = ["id"],
            childColumns = ["group_id"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["group_id"])]
)
data class Vehicle(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "model")
    val model: String,

    @ColumnInfo(name = "year")
    val year: String,

    @ColumnInfo(name = "group_id")
    val groupId: Int?,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean
)