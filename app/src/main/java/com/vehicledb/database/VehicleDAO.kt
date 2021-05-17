package com.vehicledb.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vehicledb.model.VehicleModel

@Dao
interface VehicleDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vehicleModel: VehicleModel)

    @Query("Select * from table_vehicles where key_id = :vehicleId")
     fun getVehicleDetails(vehicleId: String) : LiveData<List<VehicleModel?>>

     @Query("Select * from table_vehicles")
     fun getVehicleList() : LiveData<List<VehicleModel?>>
}
