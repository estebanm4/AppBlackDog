package com.dadm.appblackdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dadm.appblackdog.utils.Constants

@Entity(tableName = Constants.AGE_RANGES_TABLE_NAME)
data class AgeRange(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serverId: String = "",
    val max: Int = 1,
    val min: Int = 0,
    val description: String = "",
) {
}