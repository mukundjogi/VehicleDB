package com.vehicledb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vehicledb.model.VehicleModel

@Database(entities = [VehicleModel::class], version = 1, exportSchema = false)
abstract class VehicleDatabase : RoomDatabase() {
    abstract fun vehicleDAO(): VehicleDAO
    companion object{
        @Volatile
        private var databaseInstance : VehicleDatabase? = null

        fun getDatabaseInstance(context: Context?) : VehicleDatabase {
            return databaseInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context?.applicationContext!!,
                    VehicleDatabase::class.java,"vehicles.db").allowMainThreadQueries().build()
                databaseInstance = instance
                instance
            }
        }
    }
}