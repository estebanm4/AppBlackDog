package com.dadm.appblackdog.database.data

import com.dadm.appblackdog.models.AgeRange
import kotlinx.coroutines.flow.Flow

class AgeRangeOfflineRepository(private val dao: AgeRangeDao) : AgeRangeRepository {
    override fun getAllAgeRangeStream(): Flow<List<AgeRange>> = dao.getAllItems()

    override fun getAgeRangeStream(id: Int): Flow<AgeRange?> = dao.getItem(id)

    override suspend fun insertAgeRange(data: AgeRange) = dao.insert(data)

    override suspend fun deleteAgeRange(data: AgeRange) = dao.delete(data)

    override suspend fun updateAgeRange(data: AgeRange) = dao.update(data)
}