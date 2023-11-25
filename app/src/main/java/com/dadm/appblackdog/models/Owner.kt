package com.dadm.appblackdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dadm.appblackdog.utils.Constants

@Entity(tableName = Constants.OWNER_TABLE_NAME)
data class Owner(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serverId: String = "",
    val name: String = "",
    val lastname: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val hasPets: Boolean = false,
) {
}