package com.dadm.appblackdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dadm.appblackdog.utils.Constants

@Entity(tableName = Constants.REMINDER_TABLE_NAME)
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serverId: String = "",
    val name: String = "",
    val description: String = "",
    val time: Long = 0L,
) {
}