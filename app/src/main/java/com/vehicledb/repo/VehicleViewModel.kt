package com.vehicledb.repo

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vehicledb.database.VehicleDatabase
import com.vehicledb.model.VehicleModel
import kotlinx.coroutines.launch
import java.util.ArrayList

class VehicleViewModel(application: Application?) : AndroidViewModel(application!!) {
    private var repository: Repository? = null

    fun getRepository() {
        repository = Repository()
    }

    fun getDatabaseRepository(context: Context?) {
        val languageDatabase = VehicleDatabase.getDatabaseInstance(context!!)
        repository = Repository(languageDatabase.vehicleDAO())
    }

    fun insertVehicleDetails(vehicleModel: VehicleModel?) = viewModelScope.launch {
        repository?.insertVehicleDetails(vehicleModel!!)
    }

    fun getVehicleList(): LiveData<List<VehicleModel?>>? {
        return repository?.getVehicleList()
    }

    fun getVehicleMadeBy(vehicleClass: String?): MutableLiveData<ArrayList<String>>? {
        return repository?.getMadeBy(vehicleClass)!!
    }

    fun getVehicleModels(vehicleClass: String?, madeBy: String?): MutableLiveData<ArrayList<String>>? {
        return repository?.getVehicleModels(vehicleClass, madeBy)
    }

}