package com.example.sgm

import androidx.room3.Embedded
import androidx.room3.Relation

data class VehicleWithGroup(
    @Embedded
    val vehicle: Vehicle,

    @Relation(
        entity = Group::class,
        parentColumns = ["group_id"],
        entityColumns = ["id"]
    )
    val group: Group?
) {
}