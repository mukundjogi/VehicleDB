package com.vehicledb.repo

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import com.vehicledb.database.VehicleDAO
import com.vehicledb.model.VehicleModel
import com.vehicledb.network.RetrofitService
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class Repository {
    private val apiClient = RetrofitService().getApiClient()
    private var vehicleDAO: VehicleDAO? = null

    constructor()
    constructor(vehicleDAO: VehicleDAO?) {
        this.vehicleDAO = vehicleDAO!!
    }

    @WorkerThread
    suspend fun insertVehicleDetails(vehicleModel: VehicleModel?) {
        vehicleDAO?.insert(vehicleModel!!)
    }

    fun getVehicleDetails(vehicleId: String?): List<VehicleModel?>? {
        return vehicleDAO?.getVehicleDetails(vehicleId!!)?.value
    }

    fun getVehicleList(): LiveData<List<VehicleModel?>>? {
        return vehicleDAO?.getVehicleList()
    }

    fun getMadeBy(vehicleClass:String?): MutableLiveData<ArrayList<String>>? {
        val arrayList = ArrayList<String>()
        val mutableLiveData = MutableLiveData<ArrayList<String>>()
        val call: Call<Array<String>> = apiClient!!.getVehicleMadeBy(vehicleClass)
        call.enqueue(object : Callback<Array<String>> {
            override fun onResponse(call: Call<Array<String>>, response: Response<Array<String>>) {
                for (i in 0 until response.body()?.size!!){
                    arrayList.add(response.body()!![i])
                }
                mutableLiveData.postValue(arrayList)
            }
            override fun onFailure(call: Call<Array<String>>, t: Throwable) {
            }
        })
        return mutableLiveData
    }

    fun getVehicleModels(vehicleClass:String?,madeBy:String?): MutableLiveData<ArrayList<String>> {
        val arrayList = ArrayList<String>()
        val mutableLiveData = MutableLiveData<ArrayList<String>>()
        val call: Call<Array<String>> = apiClient!!.getVehicleModels(vehicleClass,madeBy)
        call.enqueue(object : Callback<Array<String>> {
            override fun onResponse(call: Call<Array<String>>, response: Response<Array<String>>) {
                for (i in 0 until response.body()?.size!!){
                    arrayList.add(response.body()!![i])
                }
                mutableLiveData.postValue(arrayList)
            }

            override fun onFailure(call: Call<Array<String>>, t: Throwable) {
            }
        })
        return mutableLiveData
    }
}