package com.dadm.appblackdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dadm.appblackdog.utils.Constants

@Entity(tableName = Constants.PET_TABLE_NAME)
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serverId: String = "",
    val ownerId: String = "",
    val name: String = "",
    val photoUrl: String = "",
    val ageRangeId: String = "",
    val ageRange: String = "",
    val description: String = "",
    val breedId: String = "",
    val breed: String = "",
    val weight: String = "",
    val measureUnitId: String = "",
    val measureUnit: String = "",
    val birthdate: Long = 0L,
) {
}
