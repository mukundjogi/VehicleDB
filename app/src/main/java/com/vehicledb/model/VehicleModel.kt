package com.vehicledb.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "table_vehicles")
data class VehicleModel(@PrimaryKey @ColumnInfo ( name = "key_id") @SerializedName("vehicle_id") var vehicleId :String = "",
                        @ColumnInfo(name = "vehicle_name")  @SerializedName("vehicle_name") var vehicleName :String? = "",
                        @ColumnInfo(name = "vehicle_class")  @SerializedName("vehicle_class") var vehicleClass :String? = "",
                        @ColumnInfo(name = "vehicle_number")  @SerializedName("vehicle_number") var vehicleNumber :String? = "",
                        @ColumnInfo(name = "made_by")  @SerializedName("made_by") var madeBy :String? = "",
                        @ColumnInfo(name = "model_name")  @SerializedName("model_name") var modelName :String? = "",
                        @ColumnInfo(name = "fuel_type")  @SerializedName("fuel_type") var fuelType :String? = "",
                        @ColumnInfo(name = "transmission_type")  @SerializedName("transmission_type") var transmissionType :String? = "") : Serializable
