package com.dadm.appblackdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dadm.appblackdog.utils.Constants

@Entity(tableName = Constants.BREEDS_TABLE_NAME)
data class Breed(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var serverId: String = "",
    var name: String = "",
    var small: Boolean = false,
) {
}