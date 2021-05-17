package com.vehicledb.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VehicleClient {
    @GET("makes")
    fun getVehicleMadeBy(
        @Query("class") vehicleClass: String?
    ): Call<Array<String>>

    @GET("models")
    fun getVehicleModels(
        @Query("class") vehicleClass: String?,
        @Query("make") madeBy: String?
    ): Call<Array<String>>
}