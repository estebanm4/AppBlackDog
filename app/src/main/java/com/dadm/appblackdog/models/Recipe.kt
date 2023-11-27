package com.dadm.appblackdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dadm.appblackdog.utils.Constants

@Entity(tableName = Constants.RECIPE_TABLE_NAME)
data class Recipe(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val serverId: String = "",
    val name: String = "",
    val description: String = "",
    val items: String = "",
    val imageUrl: String = "",
) {
}