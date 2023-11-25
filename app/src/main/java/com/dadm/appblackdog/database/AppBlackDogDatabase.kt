package com.dadm.appblackdog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dadm.appblackdog.database.data.AgeRangeDao
import com.dadm.appblackdog.database.data.BreedDao
import com.dadm.appblackdog.database.data.MeasureUnitDao
import com.dadm.appblackdog.database.data.OwnerDao
import com.dadm.appblackdog.models.AgeRange
import com.dadm.appblackdog.models.Breed
import com.dadm.appblackdog.models.MeasureUnit
import com.dadm.appblackdog.models.Owner

@Database(entities = [Owner::class, AgeRange::class, Breed::class, MeasureUnit::class], version = 8, exportSchema = false)
abstract class AppBlackDogDatabase : RoomDatabase() {
    abstract fun ownerDao(): OwnerDao
    abstract fun ageRangeDao(): AgeRangeDao
    abstract fun breedDao(): BreedDao
    abstract fun measureUnitDao(): MeasureUnitDao

    companion object {
        @Volatile
        private var Instance: AppBlackDogDatabase? = null
        fun getDatabase(context: Context): AppBlackDogDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, AppBlackDogDatabase::class.java, "black_dog_database")
                    .build()
                    .also { Instance = it }
            }
        }
    }
}