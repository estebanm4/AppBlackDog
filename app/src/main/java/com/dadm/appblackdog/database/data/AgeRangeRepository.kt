package com.dadm.appblackdog.database.data

import com.dadm.appblackdog.models.AgeRange
import kotlinx.coroutines.flow.Flow

interface AgeRangeRepository {

    fun getAllAgeRangeStream(): Flow<List<AgeRange>>

    fun getAgeRangeStream(id: Int): Flow<AgeRange?>

    suspend fun insertAgeRange(data: AgeRange)
    suspend fun insertMultipleAgeRange(data: List<AgeRange>, update: Boolean = true)

    suspend fun deleteAgeRange(data: AgeRange)

    suspend fun updateAgeRange(data: AgeRange)
}