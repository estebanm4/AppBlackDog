package com.dadm.appblackdog.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dadm.appblackdog.utils.Constants

@Entity(tableName = Constants.MEASURE_UNIT_TABLE_NAME)
data class MeasureUnit(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var serverId: String = "",
    var name: String = "",
    var abr: String = "",
    var type: String = "",
    var conversion: Double = 0.0,
    var parent: Boolean = false,
) {
}